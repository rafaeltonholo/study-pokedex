package dev.tonholo.study.pokedex.screens.pokemonList

import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
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
import androidx.paging.ExperimentalPagingApi
import coil.annotation.ExperimentalCoilApi
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import dev.tonholo.study.pokedex.R
import dev.tonholo.study.pokedex.screens.pokemonList.components.PokemonList
import dev.tonholo.study.pokedex.screens.pokemonList.components.SearchBar
import dev.tonholo.study.pokedex.ui.theme.PokedexAppThemePreview
import dev.tonholo.study.pokedex.ui.theme.state.ThemeState
import dev.tonholo.study.pokedex.ui.theme.state.ThemeStateHandler
import dev.tonholo.study.pokedex.ui.theme.viewModel.ThemeViewModel
import dev.tonholo.study.pokedex.util.preview.stubs.getPokemonListUseCaseStub
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
@Destination(
    start = true,
)
@ExperimentalCoilApi
@ExperimentalPagingApi
fun PokemonListScreen(
    navigator: DestinationsNavigator,
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
                navigator = navigator,
                viewModel = viewModel,
            )
        }
    }
}

@ExperimentalPagingApi
@ExperimentalCoilApi
@Preview
@Composable
private fun LightThemePreview() {
    PokedexAppThemePreview {
        PokemonListScreen(
            EmptyDestinationsNavigator,
            buildFakeViewModel(),
            buildPreviewThemeViewModel(ThemeState.Light),
        )
    }
}

@ExperimentalPagingApi
@ExperimentalCoilApi
@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_MASK,
)
@Composable
private fun DarkThemePreview() {
    PokedexAppThemePreview(darkTheme = true) {
        PokemonListScreen(
            EmptyDestinationsNavigator,
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

            override suspend fun retrieveFromDataStore() {
                // no-op
            }

            override suspend fun switchThemeRequest(currentTheme: ThemeState) {
                // no-op
            }
        }
    )

@ExperimentalPagingApi
private fun buildFakeViewModel(): PokemonListViewModel = PokemonListViewModel(getPokemonListUseCaseStub)
