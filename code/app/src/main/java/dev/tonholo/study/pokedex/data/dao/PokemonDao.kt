package dev.tonholo.study.pokedex.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import dev.tonholo.study.pokedex.data.entity.Pokemon
import dev.tonholo.study.pokedex.data.entity.PokemonTypePair
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface PokemonDao {
    @Transaction
    @Query("""
        SELECT * FROM Pokemon
        WHERE number > :cursor AND number < (:cursor + :offset + 1)
        LIMIT :offset
    """)
    fun getPokemonWithType(cursor: Int = 0, offset: Int): Flow<List<PokemonTypePair>>

    @Query("SELECT COUNT(1) FROM Pokemon")
    suspend fun count(): Int

    @Insert
    suspend fun insert(pokemon: Pokemon): Long

    @Insert
    suspend fun insertAll(pokemonList: List<Pokemon>)
}
