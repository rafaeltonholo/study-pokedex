package dev.tonholo.study.pokedex.data.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val POKEMON_NUMBER_COLUMN = "number"

@Entity
data class Pokemon(
    @[PrimaryKey ColumnInfo(name = POKEMON_NUMBER_COLUMN)] val number: Int,
    @NonNull val height: Int,
    @NonNull val weight: Int,
    @[NonNull ColumnInfo(name = "hp_stat")] val hpStat: Int,
    @[NonNull ColumnInfo(name = "attack_stat")] val attackStat: Int,
    @[NonNull ColumnInfo(name = "defense_stat")] val defenseStat: Int,
    @[NonNull ColumnInfo(name = "special_attack_stat")] val specialAttackStat: Int,
    @[NonNull ColumnInfo(name = "special_defense_stat")] val specialDefenseStat: Int,
    @[NonNull ColumnInfo(name = "speed_stat")] val speedStat: Int,
    @[NonNull ColumnInfo(name = "sprite_url")] val spriteUrl: String,
)
