package dev.tonholo.study.pokedex.data.entity

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val POKEMON_NUMBER_COLUMN = "number"

@Entity
data class Pokemon(
    @[PrimaryKey ColumnInfo(name = POKEMON_NUMBER_COLUMN)] val number: Int,
    @NonNull val name: String,
    @[NonNull ColumnInfo(name = "sprite_url")] val spriteUrl: String,
    @Nullable val height: Int? = null,
    @Nullable val weight: Int? = null,
    @[Nullable ColumnInfo(name = "hp_stat")] val hpStat: Int? = null,
    @[Nullable ColumnInfo(name = "attack_stat")] val attackStat: Int? = null,
    @[Nullable ColumnInfo(name = "defense_stat")] val defenseStat: Int? = null,
    @[Nullable ColumnInfo(name = "special_attack_stat")] val specialAttackStat: Int? = null,
    @[Nullable ColumnInfo(name = "special_defense_stat")] val specialDefenseStat: Int? = null,
    @[Nullable ColumnInfo(name = "speed_stat")] val speedStat: Int? = null,
    @ColumnInfo(name = "last_update") val lastUpdate: Long = System.currentTimeMillis(),
)
