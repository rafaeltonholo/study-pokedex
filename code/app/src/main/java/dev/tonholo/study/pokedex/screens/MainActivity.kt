package dev.tonholo.study.pokedex.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.tonholo.study.pokedex.ui.theme.PokedexAppTheme

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
                    }
                }
            }
        }
    }
}
