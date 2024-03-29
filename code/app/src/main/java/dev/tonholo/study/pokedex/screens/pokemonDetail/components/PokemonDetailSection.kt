package dev.tonholo.study.pokedex.screens.pokemonDetail.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import dev.tonholo.study.pokedex.R
import dev.tonholo.study.pokedex.data.remote.responses.*
import dev.tonholo.study.pokedex.ui.theme.PokedexAppThemePreview
import java.util.*

@Composable
fun PokemonDetailSection(
    pokemon: Pokemon,
    modifier: Modifier = Modifier,
    topPadding: Dp = 20.dp,
    pokemonImageSize: Dp = 200.dp,
) = with(pokemon) {
    Column(
        modifier = modifier
            .offset(y = -topPadding)
            .fillMaxSize()
            .offset(y = (pokemonImageSize / 2) - topPadding)
    ) {
        val pokemonName = name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
        Text(
            text = "#$id $pokemonName",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.fillMaxWidth(),
        )

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            PokemonTypeSection(types = types)
            PokemonDetailDataSection(
                pokemonWeight = weight,
                pokemonHeight = height,
            )
            PokemonDetailBaseStatsSection(
                stats = stats,
            )
        }
    }
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = rememberImagePainter(
                data = sprites.frontDefault,
                builder = {
                    crossfade(true)
                }
            ),
            contentDescription = stringResource(
                id = R.string.pokemon_detail_sprite_content_description,
                name,
            ),
            modifier = Modifier
                .size(pokemonImageSize)
                .offset(y = topPadding),
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun LightThemePreview() {
    PokedexAppThemePreview(darkTheme = false) {
        PokemonDetailSection(
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
    PokedexAppThemePreview(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colors.background)) {
            PokemonDetailSection(
                pokemon = previewPokemon,
            )
        }
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
    types = listOf(
        Type(
            slot = 0,
            type = TypeX(
                name = "electric",
                url = "https://pokeapi.co/api/v2/type/13/"
            ),
        ),
        Type(
            slot = 0,
            type = TypeX(
                name = "steel",
                url = "https://pokeapi.co/api/v2/type/9/"
            ),
        ),
    ),
    weight = 100,
)
