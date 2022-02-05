package dev.tonholo.study.pokedex.util.preview.stubs

import androidx.paging.ExperimentalPagingApi
import dev.tonholo.study.pokedex.usecases.GetPokemonListUseCase

@ExperimentalPagingApi
val getPokemonListUseCaseStub =
    GetPokemonListUseCase(
        RemoteMediatorStub,
        StubPokemonDao
    )
