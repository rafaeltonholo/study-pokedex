package dev.tonholo.study.pokedex.usecases

import androidx.paging.*
import dev.tonholo.study.pokedex.data.dao.PokemonDao
import dev.tonholo.study.pokedex.data.entity.PokemonTypePair
import dev.tonholo.study.pokedex.data.model.PokemonEntry
import dev.tonholo.study.pokedex.data.paging.PokemonListRemoteMediator
import dev.tonholo.study.pokedex.screens.pokemonList.PAGE_SIZE
import dev.tonholo.study.pokedex.usecases.base.UseCaseWithoutParamsSync
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ExperimentalPagingApi
class GetPokemonListUseCase @Inject constructor(
    private val remoteMediator: RemoteMediator<Int, PokemonTypePair>,
    private val pokemonDao: PokemonDao,
) : UseCaseWithoutParamsSync<Flow<PagingData<PokemonEntry>>>() {
    override fun invoke(): Flow<PagingData<PokemonEntry>> {
        val pager = Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            remoteMediator = remoteMediator,
        ) {
            pokemonDao.getPokemonWithType()
        }

        return pager.flow
            .map { pagingData ->
                pagingData.map { (pokemon, _) ->
                    PokemonEntry(
                        pokemonName = pokemon.name,
                        imageUrl = pokemon.spriteUrl,
                        number = pokemon.number,
                    )
                }
            }
    }
}
