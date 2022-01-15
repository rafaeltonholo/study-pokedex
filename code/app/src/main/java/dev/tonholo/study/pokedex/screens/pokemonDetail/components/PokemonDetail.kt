package dev.tonholo.study.pokedex.screens.pokemonDetail.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.tonholo.study.pokedex.data.remote.responses.Pokemon
import dev.tonholo.study.pokedex.data.remote.responses.Species
import dev.tonholo.study.pokedex.data.remote.responses.Sprites
import dev.tonholo.study.pokedex.ui.theme.PokedexAppTheme

@Composable
fun PokemonDetail(
    pokemon: Pokemon,
    modifier: Modifier = Modifier,
) {

}

@Preview(
    showBackground = true,
)
@Composable
private fun LightThemePreview() {
    PokedexAppTheme(darkTheme = false) {
        PokemonDetail(
            pokemon = previewPokemon,
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_MASK,
)
@Composable
private fun DarkThemePreview() {
    PokedexAppTheme(darkTheme = true) {
        PokemonDetail(
            pokemon = previewPokemon,
        )
    }
}

private val previewPokemon = Pokemon(
    abilities = listOf(),
    baseExperience = 100,
    forms = listOf(),
    gameIndices = listOf(),
    height = 100,
    heldItems = listOf(),
    id = 123,
    isDefault = true,
    locationAreaEncounters = "mock",
    moves = listOf(),
    name = "Pokemon Mock",
    order = 1,
    pastTypes = listOf(),
    species = Species("", ""),
    sprites = Sprites(
        backDefault = "mock",
        backFemale = "mock",
        backShiny = "mock",
        backShinyFemale = "mock",
        frontDefault = "mock",
        frontFemale = "mock",
        frontShiny = "mock",
        frontShinyFemale = "mock",
    ),
    stats = listOf(),
    types = listOf(),
    weight = 100,
)
