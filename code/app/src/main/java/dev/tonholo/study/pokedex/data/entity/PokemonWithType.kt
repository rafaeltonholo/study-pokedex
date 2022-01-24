package dev.tonholo.study.pokedex.data.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = [
        POKEMON_NUMBER_COLUMN,
        POKEMON_TYPE_ID_COLUMN
    ],
    indices = [
        Index(
            value = [
                POKEMON_TYPE_ID_COLUMN,
            ]
        )
    ]
)
data class PokemonWithType(
    @NonNull val number: Int,
    @NonNull @ColumnInfo(name = POKEMON_TYPE_ID_COLUMN) val typeId: Int,
)
