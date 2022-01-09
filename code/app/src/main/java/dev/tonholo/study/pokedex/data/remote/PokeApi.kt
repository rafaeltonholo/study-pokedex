package dev.tonholo.study.pokedex.data.remote

import dev.tonholo.study.pokedex.data.remote.responses.Pokemon
import dev.tonholo.study.pokedex.data.remote.responses.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Represents the PokeAPI
 * @see <a href="https://pokeapi.co/">PokeAPI docs</a>
 */
interface PokeApi {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: String,
        @Query("limit") limit: String,
    ): PokemonList

    @GET("pokemon/{name}")
    suspend fun getPokemon(
        @Path("name") name: String,
    ): Pokemon
}
