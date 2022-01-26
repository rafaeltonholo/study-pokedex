package dev.tonholo.study.pokedex.data.dao

import androidx.room.Dao
import androidx.room.Insert
import dev.tonholo.study.pokedex.data.entity.PokemonWithType

@Dao
interface PokemonWithTypeDao {
    @Insert
    fun insertAll(entities: List<PokemonWithType>)
}
