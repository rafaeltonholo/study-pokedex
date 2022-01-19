package dev.tonholo.study.pokedex.screens.pokemonList

import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import dev.tonholo.study.pokedex.R
import dev.tonholo.study.pokedex.data.remote.PokeApi
import dev.tonholo.study.pokedex.data.remote.responses.Pokemon
import dev.tonholo.study.pokedex.data.remote.responses.PokemonList
import dev.tonholo.study.pokedex.data.remote.responses.PokemonListResult
import dev.tonholo.study.pokedex.screens.pokemonList.components.PokemonList
import dev.tonholo.study.pokedex.screens.pokemonList.components.SearchBar
import dev.tonholo.study.pokedex.ui.theme.PokedexAppThemePreview
import dev.tonholo.study.pokedex.ui.theme.state.ThemeState
import dev.tonholo.study.pokedex.ui.theme.state.ThemeStateHandler
import dev.tonholo.study.pokedex.ui.theme.viewModel.ThemeViewModel
import dev.tonholo.study.pokedex.usecases.GetPokemonListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoilApi
@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
) {
    val theme by themeViewModel.theme.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
    ) {
        IconButton(onClick = {
            themeViewModel.onSwitchThemeRequest()
        }) {
            Image(
                painter = painterResource(id = when (theme) {
                    is ThemeState.Auto -> R.drawable.ic_baseline_brightness_auto_24
                    is ThemeState.Dark -> R.drawable.ic_baseline_brightness_2_24
                    is ThemeState.Light -> R.drawable.ic_baseline_brightness_high_24
                }),
                contentDescription = stringResource(id = R.string.theme_switcher_content_description),
            )
        }
        Column {
            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = stringResource(id = R.string.pokemon_logo_content_description),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )
            SearchBar(
                hint = stringResource(id = R.string.pokemon_list_search_bar_hint),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onSearch = {
                    viewModel.filterPokemonList(it)
                }
            )
            PokemonList(
                navController = navController,
                viewModel = viewModel,
            )
        }
    }
}

@ExperimentalCoilApi
@Preview
@Composable
private fun LightThemePreview() {
    PokedexAppThemePreview {
        val navController = rememberNavController()
        PokemonListScreen(
            navController,
            buildFakeViewModel(),
            buildPreviewThemeViewModel(ThemeState.Light),
        )
    }
}

@ExperimentalCoilApi
@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_MASK,
)
@Composable
private fun DarkThemePreview() {
    PokedexAppThemePreview(darkTheme = true) {
        val navController = rememberNavController()
        PokemonListScreen(
            navController,
            buildFakeViewModel(),
            buildPreviewThemeViewModel(ThemeState.Dark),
        )
    }
}

private fun buildPreviewThemeViewModel(themeState: ThemeState) =
    ThemeViewModel(
        object : ThemeStateHandler {
            override val theme: StateFlow<ThemeState>
                get() = MutableStateFlow(themeState)

            override fun switchThemeRequest(currentTheme: ThemeState) {
                // no-op
            }
        }
    )

private fun buildFakeViewModel(): PokemonListViewModel {
    val entries = (0..100).map {
        PokemonListResult(
            name = "Pokemon $it",
            url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$it.png",
        )
    }


    return PokemonListViewModel(
        GetPokemonListUseCase(object : PokeApi {
            override suspend fun getPokemonList(limit: Int, offset: Int): PokemonList = with(entries) {
                PokemonList(
                    count = size,
                    next = "mock",
                    previous = "mock",
                    results = this,
                )
            }

            override suspend fun getPokemon(name: String): Pokemon {
                TODO("Not yet implemented")
            }
        }),
    )
}
