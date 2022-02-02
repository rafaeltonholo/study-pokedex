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
abstract class PokemonDao {
    @Transaction
    @Query("""
        SELECT * FROM Pokemon
        WHERE number > :cursor AND number < (:cursor + :offset + 1)
        LIMIT :offset
    """)
    abstract fun getPokemonWithType(cursor: Int = 0, offset: Int): Flow<List<PokemonTypePair>>

    @Insert
    abstract suspend fun insert(pokemon: Pokemon): Long

    @Insert
    abstract suspend fun insertAll(pokemonList: List<Pokemon>)
}
