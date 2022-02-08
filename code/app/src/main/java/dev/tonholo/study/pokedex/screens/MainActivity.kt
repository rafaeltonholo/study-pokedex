package dev.tonholo.study.pokedex.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.paging.ExperimentalPagingApi
import coil.annotation.ExperimentalCoilApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import dagger.hilt.android.AndroidEntryPoint
import dev.tonholo.study.pokedex.screens.destinations.PokemonDetailScreenDestination
import dev.tonholo.study.pokedex.screens.destinations.PokemonListScreenDestination
import dev.tonholo.study.pokedex.screens.pokemonDetail.PokemonDetailScreen
import dev.tonholo.study.pokedex.screens.pokemonList.PokemonListScreen
import dev.tonholo.study.pokedex.ui.theme.PokedexAppTheme

@ExperimentalPagingApi
@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexAppTheme {
                DestinationsNavHost(navGraph = NavGraphs.root) {
                    composable(PokemonListScreenDestination) {
                        window.statusBarColor = MaterialTheme.colors.background.toArgb()
                        PokemonListScreen(
                            navigator = destinationsNavigator,
                        )
                    }
                    composable(PokemonDetailScreenDestination) {
                        window.statusBarColor = Color.Black.toArgb()
                        PokemonDetailScreen(
                            navArgs = navArgs,
                            navigator = destinationsNavigator,
                        )
                    }
                }
            }
        }
    }
}
