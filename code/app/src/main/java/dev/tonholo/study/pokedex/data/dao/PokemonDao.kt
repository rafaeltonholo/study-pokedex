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
    @Query("SELECT * FROM Pokemon")
    abstract fun getPokemonWithType(): Flow<List<PokemonTypePair>>

    @Insert
    abstract fun insert(pokemon: Pokemon): Long

    fun getPokemonWithTypeDistinctUntilChanged() =
        getPokemonWithType().distinctUntilChanged()
}
