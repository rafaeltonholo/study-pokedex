package dev.tonholo.study.pokedex.screens.pokemonDetail.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import dev.tonholo.study.pokedex.R
import dev.tonholo.study.pokedex.data.remote.responses.Pokemon
import dev.tonholo.study.pokedex.data.remote.responses.Species
import dev.tonholo.study.pokedex.data.remote.responses.Sprites
import dev.tonholo.study.pokedex.ui.theme.PokedexAppTheme
import dev.tonholo.study.pokedex.usecases.GetPokemonDetailUseCase

@Composable
fun PokemonDetailStateWrapper(
    state: State<GetPokemonDetailUseCase.Result>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier,
    topPadding: Dp = 20.dp,
    pokemonImageSize: Dp = 200.dp,
) {
    when (val pokemonInfo = state.value) {
        is GetPokemonDetailUseCase.Result.Success -> {
            PokemonDetail(
                pokemon = pokemonInfo.data,
                modifier = modifier,
            )
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier.fillMaxSize(),
            ) {
                with(pokemonInfo.data) {
                    Image(
                        painter = rememberImagePainter(
                            data = sprites.frontDefault,
                            builder = {
                                crossfade(true)
                            }
                        ),
                        contentDescription = stringResource(
                            id = R.string.pokemon_detail_sprint_content_description,
                            name,
                        ),
                        modifier = Modifier
                            .size(pokemonImageSize)
                            .offset(y = topPadding),
                    )
                }
            }
        }
        is GetPokemonDetailUseCase.Result.Failure -> {
            Text(
                text = pokemonInfo.message,
                color = Color.Red,
                modifier = modifier,
            )
        }
        GetPokemonDetailUseCase.Result.IsLoading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = loadingModifier,
            )
        }
    }
}


@Preview(
    showBackground = true,
)
@Composable
private fun LightThemePreview() {
    PokedexAppTheme(darkTheme = false) {
        val state = derivedStateOf {
            GetPokemonDetailUseCase.Result.Success(
                data = previewPokemon,
            )
        }
        PokemonDetailStateWrapper(
            state = state,
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
        val state = derivedStateOf {
            GetPokemonDetailUseCase.Result.Success(
                data = previewPokemon,
            )
        }
        PokemonDetailStateWrapper(
            state = state,
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
        frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
        frontFemale = "mock",
        frontShiny = "mock",
        frontShinyFemale = "mock",
    ),
    stats = listOf(),
    types = listOf(),
    weight = 100,
)
