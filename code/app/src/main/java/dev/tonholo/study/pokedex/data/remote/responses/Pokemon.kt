package dev.tonholo.study.pokedex.data.remote.responses


import com.google.gson.annotations.SerializedName
import dev.tonholo.study.pokedex.data.entity.Pokemon as EntityPokemon

data class Pokemon(
    val abilities: List<Ability>,
    @SerializedName("base_experience")
    val baseExperience: Int,
    val forms: List<Form>,
    @SerializedName("game_indices")
    val gameIndices: List<GameIndice>,
    val height: Int,
    @SerializedName("held_items")
    val heldItems: List<HeldItem>,
    val id: Int,
    @SerializedName("is_default")
    val isDefault: Boolean,
    @SerializedName("location_area_encounters")
    val locationAreaEncounters: String,
    val moves: List<Move>,
    val name: String,
    val order: Int,
    @SerializedName("past_types")
    val pastTypes: List<Any>,
    val species: Species,
    val sprites: Sprites,
    val stats: List<PokemonStat>,
    val types: List<Type>,
    val weight: Int,
)

fun Pokemon.getStat(statName: String) =
    stats.first { it.toAbbreviation().lowercase() == statName }.baseStat

fun Pokemon.toEntity() =
    EntityPokemon(
        number = id,
        name = name,
        height = height,
        weight = weight,
        hpStat = getStat("hp"),
        attackStat = getStat("atk"),
        defenseStat = getStat("def"),
        specialAttackStat = getStat("spatk"),
        specialDefenseStat = getStat("spdef"),
        speedStat = getStat("spd"),
        spriteUrl = sprites.frontDefault,
    )
