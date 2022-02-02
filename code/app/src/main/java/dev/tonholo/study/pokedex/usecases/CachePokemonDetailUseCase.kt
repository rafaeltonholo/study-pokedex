package dev.tonholo.study.pokedex.usecases

import androidx.room.withTransaction
import dev.tonholo.study.pokedex.data.database.AppDatabase
import dev.tonholo.study.pokedex.data.entity.PokemonType
import dev.tonholo.study.pokedex.data.entity.PokemonTypePair
import dev.tonholo.study.pokedex.data.entity.PokemonWithType
import dev.tonholo.study.pokedex.data.remote.responses.toEntity
import dev.tonholo.study.pokedex.usecases.base.UseCase
import javax.inject.Inject
import dev.tonholo.study.pokedex.data.remote.responses.Pokemon as RequestPokemon

class CachePokemonDetailUseCase @Inject constructor(
    private val db: AppDatabase,
) : UseCase<CachePokemonDetailUseCase.Params, CachePokemonDetailUseCase.Result>() {
    data class Params(
        val pokemon: RequestPokemon,
    )

    sealed class Result {
        object Success : Result()
        data class Failure(val message: String, val exception: Throwable) : Result()
    }

    // Can't inject directly the DAOs on constructor since I need to run them in a transaction.
    private val pokemonDao = db.pokemonDao()
    private val pokemonTypeDao = db.pokemonTypeDao()
    private val pokemonWithTypeDao = db.pokemonWithTypeDao()

    override suspend fun invoke(requestParams: Params): Result {
        val (requestPokemon) = requestParams
        val pokemonTypePair = PokemonTypePair(
            pokemon = requestPokemon.toEntity(),
            types = requestPokemon.types.map { type ->
                PokemonType(typeName = type.type.name)
            },
        )

        return try {
            db.withTransaction {
                with(pokemonTypePair) {
                    val number = pokemonDao.insert(pokemon = pokemon)
                    val pokemonWithTypeList = pokemonTypeDao.insertAll(types = types).let {
                        types.map { type ->
                            PokemonWithType(
                                number = number.toInt(),
                                typeName = type.typeName,
                            )
                        }
                    }
                    pokemonWithTypeDao.insertAll(pokemonWithTypeList)
                }
            }
            Result.Success
        } catch (e: Exception) {
            Result.Failure(
                message = e.localizedMessage ?: "Unhandled error",
                exception = e,
            )
        }
    }
}
