package dev.tonholo.study.pokedex.usecases

import android.util.Log
import dev.tonholo.study.pokedex.data.remote.PokeApi
import dev.tonholo.study.pokedex.data.remote.responses.PokemonList
import dev.tonholo.study.pokedex.usecases.base.UseCase
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "GetPokemonListUseCase"

class GetPokemonListUseCase @Inject constructor(
    private val pokeApi: PokeApi,
): UseCase<GetPokemonListUseCase.Params, GetPokemonListUseCase.Result>() {
    data class Params(
        val limit: Int = 0,
        val offset: Int = 0,
    )

    sealed class Result {
        data class Success(val data: PokemonList): Result()
        data class Failure(val message: String, val exception: Exception? = null): Result()
    }

    override suspend fun invoke(requestParams: Params): Result {
        val (limit, offset) = requestParams
        return try {
            Result.Success(pokeApi.getPokemonList(limit, offset))
        } catch (e: Exception) {
            Log.e(TAG, "invoke: Unknown exception", e)
            Result.Failure("An unknown error occurred.", e)
        }
    }
}
