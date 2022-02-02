package dev.tonholo.study.pokedex.data.remote.responses

data class PokemonList(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<PokemonListResult>,
)
