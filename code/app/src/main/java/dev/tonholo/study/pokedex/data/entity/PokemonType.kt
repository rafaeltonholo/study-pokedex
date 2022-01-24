package dev.tonholo.study.pokedex.data.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val POKEMON_TYPE_ID_COLUMN = "type_id"

@Entity(
    tableName = "Type"
)
data class PokemonType(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = POKEMON_TYPE_ID_COLUMN) val typeId: Int,
    @NonNull val name: String,
)
