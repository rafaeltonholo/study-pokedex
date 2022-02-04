package dev.tonholo.study.pokedex.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.tonholo.study.pokedex.data.entity.PokemonWithType

@Dao
interface PokemonWithTypeDao {
    @Insert
    suspend fun insertAll(entities: List<PokemonWithType>)

    @Query("DELETE FROM PokemonWithType")
    suspend fun clearAll()
}
