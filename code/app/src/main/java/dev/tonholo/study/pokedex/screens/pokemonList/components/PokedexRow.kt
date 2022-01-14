package dev.tonholo.study.pokedex.screens.pokemonList.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import dev.tonholo.study.pokedex.data.model.PokemonEntry
import dev.tonholo.study.pokedex.data.remote.PokeApi
import dev.tonholo.study.pokedex.data.remote.responses.Pokemon
import dev.tonholo.study.pokedex.data.remote.responses.PokemonList
import dev.tonholo.study.pokedex.screens.Routes
import dev.tonholo.study.pokedex.screens.pokemonList.PokemonListViewModel
import dev.tonholo.study.pokedex.ui.theme.PokedexAppTheme
import dev.tonholo.study.pokedex.ui.theme.RobotoCondensed
import dev.tonholo.study.pokedex.usecases.GetPokemonListUseCase

@ExperimentalCoilApi
@Composable
fun PokedexRow(
    rowIndex: Int,
    entries: List<PokemonEntry>,
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel(),
) {
    Column {
        Row {
            PokedexEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f),
                viewModel,
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (entries.size >= rowIndex * 2 + 2) {
                PokedexEntry(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    viewModel,
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@ExperimentalCoilApi
@Composable
private fun PokedexEntry(
    entry: PokemonEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel,
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp), true)
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor,
                    )
                )
            )
            .clickable {
                navController.navigate(
                    Routes.PokemonDetails.build(entry.pokemonName, dominantColor.toArgb())
                )
            }
    ) {
        Column {
            val painter = rememberImagePainter(
                data = entry.imageUrl,
                builder = {
                    crossfade(true)
                }
            )

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                when (val painterState = painter.state) {
                    is ImagePainter.State.Loading -> {
                        CircularProgressIndicator(
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                    is ImagePainter.State.Success -> {
                        LaunchedEffect(Unit) {
                            dominantColor = viewModel.getDominantColor(painterState.result.drawable)
                        }
                    }
                    else -> {
                        // no-op
                    }
                }
                Image(
                    painter = painter,
                    contentDescription = entry.pokemonName,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                )
            }

            Text(
                text = "#${entry.number} - ${entry.pokemonName}",
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private object StubPokemonApi : PokeApi {
    override suspend fun getPokemonList(limit: Int, offset: Int): PokemonList {
        throw NotImplementedError()
    }

    override suspend fun getPokemon(name: String): Pokemon {
        throw NotImplementedError()
    }

}

@ExperimentalCoilApi
@Preview(
    showBackground = true,
)
@Composable
private fun LightThemePreview() {
    PokedexAppTheme {
        val navController = rememberNavController()
        val entries = (0..2).map {
            PokemonEntry(
                "Pokemon $it",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$it.png",
                number = it,
            )
        }
        val itemCount = if (entries.size % 2 == 0) {
            entries.size / 2
        } else {
            entries.size / 2 + 1
        }
        Column(modifier = Modifier.padding(32.dp)) {
            for (index in 0 until itemCount) {
                PokedexRow(
                    rowIndex = index,
                    entries = entries,
                    navController = navController,
                    viewModel = PokemonListViewModel(
                        GetPokemonListUseCase(StubPokemonApi),
                    ),
                )
            }
        }
    }
}

@ExperimentalCoilApi
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_MASK,
)
@Composable
private fun DarkThemePreview() {
    PokedexAppTheme(darkTheme = true) {
        val navController = rememberNavController()
        val entries = (0 until 4).map {
            PokemonEntry(
                "Pokemon $it",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$it.png",
                number = it,
            )
        }
        val itemCount = if (entries.size % 2 == 0) {
            entries.size / 2
        } else {
            entries.size / 2 + 1
        }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .padding(32.dp)
        ) {
            for (index in 0 until itemCount) {
                PokedexRow(
                    rowIndex = index,
                    entries = entries,
                    navController = navController,
                    viewModel = PokemonListViewModel(
                        GetPokemonListUseCase(StubPokemonApi),
                    ),
                )
            }
        }
    }
}
