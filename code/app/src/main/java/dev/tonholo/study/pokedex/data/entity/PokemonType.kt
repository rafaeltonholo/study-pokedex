package dev.tonholo.study.pokedex.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val POKEMON_TYPE_PK_COLUMN = "name"

@Entity(
    tableName = "Type"
)
data class PokemonType(
    @PrimaryKey
    @ColumnInfo(name = POKEMON_TYPE_PK_COLUMN)
    val typeName: String,
)
