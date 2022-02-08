package dev.tonholo.study.pokedex.util.preview.stubs

import androidx.paging.PagingSource
import dev.tonholo.study.pokedex.data.dao.PokemonDao
import dev.tonholo.study.pokedex.data.entity.Pokemon
import dev.tonholo.study.pokedex.data.entity.PokemonTypePair
import kotlinx.coroutines.flow.Flow

object StubPokemonDao : PokemonDao {
    override fun getPokemonWithType(): PagingSource<Int, PokemonTypePair> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(pokemon: Pokemon): Long {
        TODO("Not yet implemented")
    }

    override suspend fun insertAll(pokemonList: List<Pokemon>) {
        TODO("Not yet implemented")
    }

    override suspend fun clearAll() {
        TODO("Not yet implemented")
    }

    override suspend fun lastUpdated(): Long {
        TODO("Not yet implemented")
    }
}
