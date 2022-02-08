package dev.tonholo.study.pokedex.usecases

import android.net.Uri
import dev.tonholo.study.pokedex.data.dao.RemoteKeyDao
import dev.tonholo.study.pokedex.data.entity.RemoteKey
import dev.tonholo.study.pokedex.data.remote.responses.PokemonList
import dev.tonholo.study.pokedex.data.remote.responses.number
import dev.tonholo.study.pokedex.usecases.base.UseCase
import javax.inject.Inject

class UpsertRemoteKeyUseCase @Inject constructor(
    private val remoteKeyDao: RemoteKeyDao,
): UseCase<PokemonList, UpsertRemoteKeyUseCase.Result>() {
    sealed class Result {
        object Success : Result()
        data class Failure(val message: String, val exception: Throwable) : Result()
    }

    override suspend fun invoke(requestParams: PokemonList): Result = try {
        requestParams.run {
            val nextKey = next?.let {
                Uri.parse(it).getQueryParameter("offset")?.toInt()
            }

            remoteKeyDao.upsert(RemoteKey(
                pokemonNumber = results.last().number,
                nextKey = nextKey,
            ))
        }
        Result.Success
    } catch (e: Exception) {
        Result.Failure(
            message = e.localizedMessage ?: "Unhandled error",
            exception = e,
        )
    }
}
