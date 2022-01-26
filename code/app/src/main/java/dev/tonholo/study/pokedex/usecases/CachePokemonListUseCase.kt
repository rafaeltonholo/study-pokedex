package dev.tonholo.study.pokedex.usecases

import androidx.room.withTransaction
import dev.tonholo.study.pokedex.data.database.AppDatabase
import dev.tonholo.study.pokedex.data.entity.Pokemon
import dev.tonholo.study.pokedex.data.entity.PokemonType
import dev.tonholo.study.pokedex.data.entity.PokemonTypePair
import dev.tonholo.study.pokedex.data.entity.PokemonWithType
import dev.tonholo.study.pokedex.data.remote.responses.toAbbreviation
import dev.tonholo.study.pokedex.usecases.base.UseCase
import javax.inject.Inject
import dev.tonholo.study.pokedex.data.remote.responses.Pokemon as RequestPokemon

class CachePokemonListUseCase @Inject constructor(
    private val db: AppDatabase,
) : UseCase<CachePokemonListUseCase.Params, CachePokemonListUseCase.Result>() {
    data class Params(
        val data: List<RequestPokemon>,
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
        val (data) = requestParams
        val pokemonWithTypeList = data.map { pokemon ->
            fun getStat(pokemon: RequestPokemon, statName: String) =
                pokemon.stats.first { it.toAbbreviation().lowercase() == statName }.baseStat

            PokemonTypePair(
                pokemon = Pokemon(
                    number = pokemon.id,
                    height = pokemon.height,
                    weight = pokemon.weight,
                    hpStat = getStat(pokemon, "hp"),
                    attackStat = getStat(pokemon, "atk"),
                    defenseStat = getStat(pokemon, "def"),
                    specialAttackStat = getStat(pokemon, "spatk"),
                    specialDefenseStat = getStat(pokemon, "spdef"),
                    speedStat = getStat(pokemon, "spd"),
                    spriteUrl = pokemon.sprites.frontDefault,
                ),
                types = pokemon.types.map { type ->
                    PokemonType(typeName = type.type.name)
                },
            )
        }

        return try {
            db.withTransaction {
                val pokemonWithType = pokemonWithTypeList.flatMap { (pokemon, types) ->
                    val number = pokemonDao.insert(pokemon = pokemon)
                    pokemonTypeDao.insertAll(types = types).let {
                        types.map { type ->
                            PokemonWithType(
                                number = number.toInt(),
                                typeName = type.typeName,
                            )
                        }
                    }
                }
                pokemonWithTypeDao.insertAll(pokemonWithType)
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
