package dev.tonholo.study.pokedex.util.preview.stubs

import dev.tonholo.study.pokedex.data.remote.PokeApi
import dev.tonholo.study.pokedex.data.remote.responses.Pokemon
import dev.tonholo.study.pokedex.data.remote.responses.PokemonList

object StubPokemonApi : PokeApi {
    override suspend fun getPokemonList(limit: Int, offset: Int): PokemonList {
        throw NotImplementedError()
    }

    override suspend fun getPokemon(name: String): Pokemon {
        throw NotImplementedError()
    }
}
