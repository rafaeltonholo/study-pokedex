package dev.tonholo.study.pokedex.usecases

import dev.tonholo.study.pokedex.data.dao.PokemonDao
import dev.tonholo.study.pokedex.data.entity.Pokemon
import dev.tonholo.study.pokedex.data.remote.responses.PokemonListResult
import dev.tonholo.study.pokedex.data.remote.responses.number
import dev.tonholo.study.pokedex.usecases.base.UseCase
import dev.tonholo.study.pokedex.util.toTitleCase
import javax.inject.Inject

class CachePokemonListUseCase @Inject constructor(
    private val pokemonDao: PokemonDao,
) : UseCase<List<PokemonListResult>, CachePokemonListUseCase.Result>() {

    sealed class Result {
        object Success : Result()
        data class Failure(val message: String, val exception: Throwable) : Result()
    }

    override suspend fun invoke(requestParams: List<PokemonListResult>): Result = try {
        pokemonDao.insertAll(
            requestParams.map { entry ->
                val number = entry.number
                val spriteUrl =
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                Pokemon(
                    number = number,
                    name = entry.name.toTitleCase(),
                    spriteUrl = spriteUrl,
                )
            }
        )
        Result.Success
    } catch (e: Exception) {
        Result.Failure(
            message = e.localizedMessage ?: "Unhandled error",
            exception = e,
        )
    }
}
