package dev.tonholo.study.pokedex.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKey(
    @[PrimaryKey ColumnInfo(name = "pokemon_number")]
    val pokemonNumber: Int,
    val nextKey: Int?,
)
