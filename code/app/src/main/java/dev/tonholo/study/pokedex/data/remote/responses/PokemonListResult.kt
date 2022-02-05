package dev.tonholo.study.pokedex.data.remote.responses


data class PokemonListResult(
    val name: String,
    val url: String,
)

val PokemonListResult.number
    get() = if (url.endsWith("/")) {
        url.dropLast(1).takeLastWhile { it.isDigit() }
    } else {
        url.takeLastWhile { it.isDigit() }
    }.toInt()
