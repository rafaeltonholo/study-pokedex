package dev.tonholo.study.pokedex.screens

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import dagger.hilt.android.AndroidEntryPoint
import dev.tonholo.study.pokedex.R
import dev.tonholo.study.pokedex.screens.pokemonDetail.PokemonDetailScreen
import dev.tonholo.study.pokedex.screens.pokemonList.PokemonListScreen
import dev.tonholo.study.pokedex.ui.theme.PokedexAppTheme

@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.PokemonList.route,
                ) {
                    composable(Routes.PokemonList.route) {
                        window.statusBarColor = MaterialTheme.colors.background.toArgb()
                        PokemonListScreen(navController)
                    }
                    composable(
                        route = Routes.PokemonDetails.route,
                        arguments = listOf(
                            navArgument(Routes.PokemonDetails.Params.pokemonName) {
                                type = NavType.StringType
                            },
                            navArgument(Routes.PokemonDetails.Params.dominantColor) {
                                type = NavType.IntType
                            }
                        ),
                    ) {
                        window.statusBarColor = ContextCompat.getColor(this@MainActivity, android.R.color.black)
                        val pokemonName = remember {
                            it.arguments?.getString(Routes.PokemonDetails.Params.pokemonName)
                                ?: throw IllegalArgumentException(
                                    "Can't access ${Routes.PokemonDetails.baseRoute} without ${
                                        Routes.PokemonDetails.Params.pokemonName
                                    } parameter"
                                )
                        }
                        val dominantColor = remember {
                            val color = it.arguments?.getInt(Routes.PokemonDetails.Params.dominantColor)
                                ?: throw IllegalArgumentException(
                                    "Can't access ${Routes.PokemonDetails.baseRoute} without ${
                                        Routes.PokemonDetails.Params.dominantColor
                                    } parameter"
                                )
                            Color(color)
                        }

                        PokemonDetailScreen(
                            dominantColor = dominantColor,
                            pokemonName = pokemonName,
                            navController = navController,
                        )
                    }
                }
            }
        }
    }
}
