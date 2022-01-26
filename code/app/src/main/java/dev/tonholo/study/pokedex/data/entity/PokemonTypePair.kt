package dev.tonholo.study.pokedex.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PokemonTypePair(
    @Embedded val pokemon: Pokemon,
    @Relation(
        parentColumn = POKEMON_NUMBER_COLUMN,
        entity = PokemonType::class,
        entityColumn = POKEMON_TYPE_PK_COLUMN,
        associateBy = Junction(PokemonWithType::class),
    )
    val types: List<PokemonType>
)
