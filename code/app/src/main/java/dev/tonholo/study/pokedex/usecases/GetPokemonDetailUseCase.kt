package dev.tonholo.study.pokedex.usecases

import android.util.Log
import dev.tonholo.study.pokedex.data.remote.PokeApi
import dev.tonholo.study.pokedex.data.remote.responses.Pokemon
import dev.tonholo.study.pokedex.usecases.base.UseCase
import javax.inject.Inject

private const val TAG = "GetPokemonDetailUseCase"

class GetPokemonDetailUseCase @Inject constructor(
    private val pokeApi: PokeApi,
) : UseCase<String, GetPokemonDetailUseCase.Result>() {
    sealed class Result {
        data class Success(val data: Pokemon): Result()
        data class Failure(val message: String, val exception: Exception? = null): Result()
        object IsLoading : Result()
    }

    /**
     * @param requestParams The Pokemon's name
     */
    override suspend fun invoke(requestParams: String): Result = try {
        Result.Success(pokeApi.getPokemon(requestParams.lowercase()))
    } catch (e: Exception) {
        Log.e(TAG, "invoke: Unknown exception", e)
        Result.Failure("An unknown error occurred.", e)
    }
}
