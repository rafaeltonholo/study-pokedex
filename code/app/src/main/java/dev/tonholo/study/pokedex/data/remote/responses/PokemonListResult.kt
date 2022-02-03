package dev.tonholo.study.pokedex.data.remote.responses


data class PokemonListResult(
    val name: String,
    val url: String,
    val number: Int? = null,
)
