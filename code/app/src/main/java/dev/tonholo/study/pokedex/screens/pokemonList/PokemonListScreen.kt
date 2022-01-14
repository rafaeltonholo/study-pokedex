package dev.tonholo.study.pokedex.screens.pokemonList

import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
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
import dev.tonholo.study.pokedex.ui.theme.PokedexAppTheme
import dev.tonholo.study.pokedex.usecases.GetPokemonListUseCase

@ExperimentalCoilApi
@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel(),
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize(),
    ) {
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
                    .padding(16.dp)
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
    PokedexAppTheme {
        val navController = rememberNavController()
        PokemonListScreen(navController)
    }
}

@ExperimentalCoilApi
@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_MASK,
)
@Composable
private fun DarkThemePreview() {
    PokedexAppTheme(darkTheme = true) {
        val navController = rememberNavController()
        PokemonListScreen(navController, buildFakeViewModel())
    }
}

private fun buildFakeViewModel(): PokemonListViewModel {
    val entries = (0..2).map {
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
