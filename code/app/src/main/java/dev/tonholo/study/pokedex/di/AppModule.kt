package dev.tonholo.study.pokedex.di

import android.app.Application
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.tonholo.study.pokedex.data.remote.PokeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://pokeapi.co/api/v2/")
            .build()

    @Provides
    fun providePokeApi(retrofit: Retrofit): PokeApi =
        retrofit.create(PokeApi::class.java)

    @Provides
    fun provideResources(application: Application): Resources = application.resources
}
