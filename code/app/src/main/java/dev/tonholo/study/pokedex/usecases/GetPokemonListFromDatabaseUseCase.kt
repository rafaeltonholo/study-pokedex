package dev.tonholo.study.pokedex.usecases

import dev.tonholo.study.pokedex.data.dao.PokemonDao
import dev.tonholo.study.pokedex.data.remote.responses.PokemonList
import dev.tonholo.study.pokedex.data.remote.responses.PokemonListResult
import dev.tonholo.study.pokedex.usecases.base.UseCaseFlowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPokemonListFromDatabaseUseCase @Inject constructor(
    private val pokemonDao: PokemonDao,
) : UseCaseFlowable<GetPokemonListFromDatabaseUseCase.Params, PokemonList>() {
    data class Params(
        val cursor: Int = 0,
        val offset: Int = 0,
    )

    override fun invoke(requestParams: Params): Flow<PokemonList> = requestParams.run {
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
    }
}
