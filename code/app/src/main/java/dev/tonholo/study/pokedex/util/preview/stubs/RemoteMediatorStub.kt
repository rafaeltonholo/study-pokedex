package dev.tonholo.study.pokedex.util.preview.stubs

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dev.tonholo.study.pokedex.data.entity.PokemonTypePair

@ExperimentalPagingApi
object RemoteMediatorStub : RemoteMediator<Int, PokemonTypePair>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, PokemonTypePair>): MediatorResult {
        TODO("Not yet implemented")
    }
}
