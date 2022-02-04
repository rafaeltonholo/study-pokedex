package dev.tonholo.study.pokedex.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.tonholo.study.pokedex.data.database.AppDatabase
import dev.tonholo.study.pokedex.data.model.PokemonEntry
import dev.tonholo.study.pokedex.data.remote.PokeApi
import dev.tonholo.study.pokedex.screens.pokemonList.PAGE_SIZE
import dev.tonholo.study.pokedex.usecases.CachePokemonListUseCase
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalPagingApi
class PokemonListRemoteMediator @Inject constructor(
    private val database: AppDatabase,
    private val pokeApi: PokeApi,
    private val cachePokemonListUseCase: CachePokemonListUseCase,
) : RemoteMediator<Int, PokemonEntry>() {
    private val pokemonDao = database.pokemonDao()
    private val pokemonWithTypeDao = database.pokemonWithTypeDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntry>,
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    lastItem.number
                }
            }

            val response = pokeApi.getPokemonList(
                limit = PAGE_SIZE,
                offset = (loadKey ?: 0) * PAGE_SIZE,
            )

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pokemonWithTypeDao.clearAll()
                    pokemonDao.clearAll()
                }

                val result = cachePokemonListUseCase(response.results)

                if (result is CachePokemonListUseCase.Result.Failure) {
                    throw result.exception
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = response.next == null
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
