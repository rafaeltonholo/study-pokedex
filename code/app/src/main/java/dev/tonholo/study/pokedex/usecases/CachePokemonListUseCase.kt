package dev.tonholo.study.pokedex.usecases

import dev.tonholo.study.pokedex.data.dao.PokemonDao
import dev.tonholo.study.pokedex.data.entity.Pokemon
import dev.tonholo.study.pokedex.data.model.PokemonEntry
import dev.tonholo.study.pokedex.usecases.base.UseCase
import javax.inject.Inject

class CachePokemonListUseCase @Inject constructor(
    private val pokemonDao: PokemonDao,
) : UseCase<List<PokemonEntry>, CachePokemonListUseCase.Result>() {

    sealed class Result {
        object Success : Result()
        data class Failure(val message: String, val exception: Throwable) : Result()
    }

    override suspend fun invoke(requestParams: List<PokemonEntry>): Result {
        return try {
            pokemonDao.insertAll(
                requestParams.map { entry ->
                    Pokemon(
                        number = entry.number,
                        name = entry.pokemonName,
                        spriteUrl = entry.imageUrl,
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
}
