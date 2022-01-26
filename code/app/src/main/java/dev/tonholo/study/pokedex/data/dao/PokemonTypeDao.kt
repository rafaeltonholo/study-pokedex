package dev.tonholo.study.pokedex.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import dev.tonholo.study.pokedex.data.entity.PokemonType

@Dao
interface PokemonTypeDao {
    @Insert(onConflict = IGNORE)
    suspend fun insertAll(types: List<PokemonType>)
}
