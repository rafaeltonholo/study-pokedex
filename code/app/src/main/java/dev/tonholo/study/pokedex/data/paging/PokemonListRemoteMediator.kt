package dev.tonholo.study.pokedex.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.tonholo.study.pokedex.data.database.AppDatabase
import dev.tonholo.study.pokedex.data.entity.PokemonTypePair
import dev.tonholo.study.pokedex.data.remote.PokeApi
import dev.tonholo.study.pokedex.usecases.CachePokemonListUseCase
import dev.tonholo.study.pokedex.usecases.UpsertRemoteKeyUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "RemoteMediator"
private const val CACHE_TIMEOUT_IN_DAYS = 10L

@ExperimentalPagingApi
class PokemonListRemoteMediator @Inject constructor(
    private val database: AppDatabase,
    private val pokeApi: PokeApi,
    private val cachePokemonListUseCase: CachePokemonListUseCase,
    private val upsertRemoteKeyUseCase: UpsertRemoteKeyUseCase,
) : RemoteMediator<Int, PokemonTypePair>() {
    private val pokemonDao = database.pokemonDao()
    private val pokemonWithTypeDao = database.pokemonWithTypeDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val now = System.currentTimeMillis()
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(CACHE_TIMEOUT_IN_DAYS, TimeUnit.DAYS)
        Log.d(TAG, "initialize: cacheTimeout = $cacheTimeout")

        val lastUpdated = withContext(Dispatchers.IO) {
            pokemonDao.lastUpdated()
        } ?: now - cacheTimeout
        Log.d(TAG, "initialize: lastUpdated = $lastUpdated")
        Log.d(TAG, "initialize: now = $now")
        val shouldSkipCacheInvalidation = now - lastUpdated < cacheTimeout
        Log.d(TAG, "initialize: shouldSkipCacheInvalidation = $shouldSkipCacheInvalidation")
        return if (shouldSkipCacheInvalidation) {
            // Cached data is up-to-date, so there is no need to re-fetch from network.
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // Need to refresh cached data from network; returning LAUNCH_INITIAL_REFRESH here
            // will also block RemoteMediator's APPEND and PREPEND from running until REFRESH
            // succeeds.
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonTypePair>,
    ): MediatorResult {
        Log.d(TAG, "load() called with: loadType = $loadType, state = $state")

        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = false)

                    Log.d(TAG, "load: lastItem = $lastItem")

                    val remoteKey = database.withTransaction {
                        remoteKeyDao.get(lastItem.pokemon.number)
                    }

                    Log.d(TAG, "load: remoteKey = $remoteKey")
                    remoteKey.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = pokeApi.getPokemonList(
                limit = state.config.pageSize,
                offset = loadKey ?: 0,
            )

            Log.d(TAG, "load: remote response = $response")

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    Log.i(TAG, "load: Refresh requested; cleaning all data from Pokemon")
                    pokemonWithTypeDao.clearAll()
                    pokemonDao.clearAll()
                    remoteKeyDao.clearAll()
                }

                Log.d(TAG, "load: inserting/updating remote key")
                upsertRemoteKeyUseCase(response).run {
                    Log.d(TAG, "upsertRemoteKeyUseCase.run: upsert result = $this")
                    if (this is UpsertRemoteKeyUseCase.Result.Failure) {
                        throw exception
                    }
                }

                cachePokemonListUseCase(response.results).run {
                    Log.d(TAG, "cachePokemonListUseCase.run: cache result = $this")
                    if (this is CachePokemonListUseCase.Result.Failure) {
                        throw exception
                    }
                }
            }

            val endOfPaginationReached = response.next == null
            Log.d(TAG, "load: endOfPaginationReached = $endOfPaginationReached")

            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            Log.e(TAG, "load: Mediator error.", e)
            MediatorResult.Error(e)
        }
    }
}
