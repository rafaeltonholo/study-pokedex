package dev.tonholo.study.pokedex.data.dao

import androidx.paging.PagingSource
import androidx.room.*
import dev.tonholo.study.pokedex.data.entity.Pokemon
import dev.tonholo.study.pokedex.data.entity.PokemonTypePair

@Dao
interface PokemonDao {
    @Transaction
    @Query("SELECT * FROM Pokemon")
    fun getPokemonWithType(): PagingSource<Int, PokemonTypePair>

    @Insert
    suspend fun insert(pokemon: Pokemon): Long

    @Insert
    suspend fun insertAll(pokemonList: List<Pokemon>)

    @Query("DELETE FROM Pokemon")
    suspend fun clearAll()

    @Query("SELECT MAX(last_update) FROM Pokemon")
    suspend fun lastUpdated(): Long?
}
