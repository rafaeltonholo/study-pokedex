package dev.tonholo.study.pokedex.screens

object Routes {
    object PokemonList {
        const val route = "pokemon_list_screen"
    }

    object PokemonDetails {
        object Params {
            const val dominantColor = "dominantColor"
            const val pokemonName = "pokemonName"
        }

        const val baseRoute = "pokemon_detail_screen"
        const val route =
            "$baseRoute/{${Params.pokemonName}}?${Params.dominantColor}={${Params.dominantColor}}"

        fun build(pokemonName: String, dominantColor: Int) =
            "$baseRoute/${pokemonName.lowercase()}?${Params.dominantColor}=${dominantColor}"
    }
}
