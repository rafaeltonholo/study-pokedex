package dev.tonholo.study.pokedex.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.tonholo.study.pokedex.data.dao.PokemonDao
import dev.tonholo.study.pokedex.data.dao.PokemonTypeDao
import dev.tonholo.study.pokedex.data.dao.PokemonWithTypeDao
import dev.tonholo.study.pokedex.data.database.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase(context)

    @Provides
    fun providePokemonDao(appDatabase: AppDatabase): PokemonDao =
        appDatabase.pokemonDao()

    @Provides
    fun providePokemonTypeDao(appDatabase: AppDatabase): PokemonTypeDao =
        appDatabase.pokemonTypeDao()

    @Provides
    fun providePokemonWithTypeDao(appDatabase: AppDatabase): PokemonWithTypeDao =
        appDatabase.pokemonWithTypeDao()
}
