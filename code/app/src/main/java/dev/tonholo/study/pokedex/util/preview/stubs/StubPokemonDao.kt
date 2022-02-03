package dev.tonholo.study.pokedex.util.preview.stubs

import dev.tonholo.study.pokedex.data.dao.PokemonDao
import dev.tonholo.study.pokedex.data.entity.PokemonTypePair
import kotlinx.coroutines.flow.Flow

object StubPokemonDao : PokemonDao {
    override fun getPokemonWithType(cursor: Int, offset: Int): Flow<List<PokemonTypePair>> {
        TODO("Not yet implemented")
    }

    override suspend fun count(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun insert(pokemon: dev.tonholo.study.pokedex.data.entity.Pokemon): Long {
        TODO("Not yet implemented")
    }

    override suspend fun insertAll(pokemonList: List<dev.tonholo.study.pokedex.data.entity.Pokemon>) {
        TODO("Not yet implemented")
    }
}
