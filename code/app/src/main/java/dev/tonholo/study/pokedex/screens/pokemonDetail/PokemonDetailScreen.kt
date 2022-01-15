package dev.tonholo.study.pokedex.screens.pokemonDetail

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.tonholo.study.pokedex.data.remote.PokeApi
import dev.tonholo.study.pokedex.data.remote.responses.Pokemon
import dev.tonholo.study.pokedex.data.remote.responses.PokemonList
import dev.tonholo.study.pokedex.data.remote.responses.Species
import dev.tonholo.study.pokedex.data.remote.responses.Sprites
import dev.tonholo.study.pokedex.screens.pokemonDetail.components.PokemonDetailNavigationBar
import dev.tonholo.study.pokedex.screens.pokemonDetail.components.PokemonDetailStateWrapper
import dev.tonholo.study.pokedex.ui.theme.PokedexAppTheme
import dev.tonholo.study.pokedex.ui.theme.Purple200
import dev.tonholo.study.pokedex.usecases.GetPokemonDetailUseCase

@Composable
fun PokemonDetailScreen(
    dominantColor: Color,
    pokemonName: String,
    navController: NavController,
    topPadding: Dp = 20.dp,
    pokemonImageSize: Dp = 200.dp,
    viewModel: PokemonDetailViewModel = hiltViewModel(),
) {
    val pokemonDetailState = produceState<GetPokemonDetailUseCase.Result>(
        initialValue = GetPokemonDetailUseCase.Result.IsLoading
    ) {
        value = viewModel.getPokemonDetail(pokemonName)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(bottom = 16.dp)
    ) {
        PokemonDetailNavigationBar(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopStart)
        )

        val margin = remember { // remember to avoid unnecessary calculations during the recomposition.
            PaddingValues(
                top = topPadding + (pokemonImageSize / 2f),
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp,
            )
        }
        PokemonDetailStateWrapper(
            state = pokemonDetailState,
            modifier = Modifier
                .fillMaxSize()
                .padding(margin) // first padding will act as a margin.
                .shadow(10.dp, RoundedCornerShape(12.dp), true)
                .background(MaterialTheme.colors.surface)
                .padding(16.dp) // after drawing the above box, second padding is actually a padding.
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(margin),
            topPadding = topPadding,
            pokemonImageSize = pokemonImageSize,
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun LightThemePreview() {
    PokedexAppTheme(darkTheme = false) {
        val navController = rememberNavController()
        PokemonDetailScreen(
            dominantColor = Purple200,
            pokemonName = "",
            navController = navController,
            viewModel = buildPreviewViewModel(),
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
        val navController = rememberNavController()
        PokemonDetailScreen(
            dominantColor = Purple200,
            pokemonName = "",
            navController = navController,
            viewModel = buildPreviewViewModel(),
        )
    }
}

private fun buildPreviewViewModel() = PokemonDetailViewModel(
    GetPokemonDetailUseCase(object : PokeApi {
        override suspend fun getPokemonList(limit: Int, offset: Int): PokemonList {
            throw NotImplementedError()
        }

        override suspend fun getPokemon(name: String): Pokemon = Pokemon(
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
    })
)
