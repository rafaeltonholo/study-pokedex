package dev.tonholo.study.pokedex.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dev.tonholo.study.pokedex.data.entity.PokemonTypePair
import dev.tonholo.study.pokedex.data.paging.PokemonListRemoteMediator

@ExperimentalPagingApi
@Module
@InstallIn(ViewModelComponent::class)
interface ViewModelModule {
    @Binds
    @ViewModelScoped
    fun bindRemoteMediator(pokemonListRemoteMediator: PokemonListRemoteMediator): RemoteMediator<Int, PokemonTypePair>
}
