package dev.tonholo.study.pokedex.usecases

import android.util.Log
import dev.tonholo.study.pokedex.data.dao.PokemonDao
import dev.tonholo.study.pokedex.data.remote.responses.PokemonList
import dev.tonholo.study.pokedex.data.remote.responses.PokemonListResult
import dev.tonholo.study.pokedex.usecases.base.UseCaseFlowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG = "GetPokemonListFromDB"

class GetPokemonListFromDatabaseUseCase @Inject constructor(
    private val pokemonDao: PokemonDao,
) : UseCaseFlowable<GetPokemonListFromDatabaseUseCase.Params, GetPokemonListFromDatabaseUseCase.Result>() {
    data class Params(
        val cursor: Int = 0,
        val offset: Int = 0,
    )

    sealed class Result(open val data: PokemonList? = null) {
        data class Success(override val data: PokemonList) : Result(data)
        object IsLoading : Result()
        data class Failure(val message: String, val exception: Exception? = null) : Result()
    }

    override fun invoke(requestParams: Params): Flow<Result> = flow {
        val (cursor, offset) = requestParams
        emit(Result.IsLoading)

        try {
            emitAll(
                pokemonDao.getPokemonWithType(cursor, offset)
                    .map {
                        PokemonList(
                            count = pokemonDao.count(),
                            results = it.map { (pokemon, _) ->
                                PokemonListResult(
                                    name = pokemon.name,
                                    url = pokemon.spriteUrl,
                                    number = pokemon.number,
                                )
                            }
                        )
                    }
                    .map { Result.Success(it) }
            )
        } catch (e: Exception) {
            Log.e(TAG, "invoke: Unknown exception", e)
            emit(Result.Failure("An unknown error occurred.", e))
        }
    }
}
