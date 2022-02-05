package dev.tonholo.study.pokedex.screens.pokemonList.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.ExperimentalPagingApi
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import dev.tonholo.study.pokedex.R
import dev.tonholo.study.pokedex.data.model.PokemonEntry
import dev.tonholo.study.pokedex.screens.Routes
import dev.tonholo.study.pokedex.screens.pokemonList.PokemonListViewModel
import dev.tonholo.study.pokedex.ui.theme.PokedexAppThemePreview
import dev.tonholo.study.pokedex.ui.theme.RobotoCondensed
import dev.tonholo.study.pokedex.util.preview.stubs.getPokemonListUseCaseStub
import dev.tonholo.study.pokedex.util.toGreyscale

@ExperimentalPagingApi
@ExperimentalCoilApi
@Composable
fun PokedexRow(
    first: PokemonEntry,
    last: PokemonEntry?,
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel(),
) {
    Column {
        Row {
            PokedexEntry(
                entry = first,
                navController = navController,
                modifier = Modifier.weight(1f),
                viewModel,
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (last != null) {
                PokedexEntry(
                    entry = last,
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

@ExperimentalPagingApi
@ExperimentalCoilApi
@Composable
private fun PokedexEntry(
    entry: PokemonEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel,
) {
    var colorAnimationPlayed by remember { mutableStateOf(false) }
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }
    val animDurationMillis = 500
    val currentColor by animateColorAsState(
        targetValue = if (!colorAnimationPlayed) MaterialTheme.colors.onSurface else dominantColor,
        animationSpec = tween(durationMillis = animDurationMillis)
    )

    val context = LocalContext.current
    val placeholder = remember {
        ContextCompat.getDrawable(context, R.drawable.ic_pokeball)
            ?.apply {
                toGreyscale()
            }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp), true)
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        currentColor,
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
                    crossfade(animDurationMillis)
                    placeholder(placeholder)
                }
            )

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painter,
                    contentDescription = entry.pokemonName,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                )
                when (val painterState = painter.state) {
                    is ImagePainter.State.Loading -> {
                        CircularProgressIndicator(
                            color = Color.Red,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                    is ImagePainter.State.Success -> {
                        LaunchedEffect(Unit) {
                            colorAnimationPlayed = true
                        }
                        dominantColor = viewModel.getDominantColor(painterState.result.drawable)
                    }
                    else -> {
                        // no-op
                    }
                }
            }

            Text(
                text = "#${entry.number} - ${entry.pokemonName}",
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@ExperimentalPagingApi
@ExperimentalCoilApi
@Preview(
    showBackground = true,
)
@Composable
private fun LightThemePreview() {
    PokedexAppThemePreview {
        val navController = rememberNavController()
        val entries = (0..2).map {
            PokemonEntry(
                "Pokemon $it",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$it.png",
                number = it,
            )
        }

        Column(modifier = Modifier.padding(32.dp)) {
            for (index in entries.indices step 2) {
                PokedexRow(
                    first = entries[index],
                    last = if (index + 1 >= entries.size) null else entries[index + 1],
                    navController = navController,
                    viewModel = PokemonListViewModel(
                        getPokemonListUseCaseStub
                    ),
                )
            }
        }
    }
}

@ExperimentalPagingApi
@ExperimentalCoilApi
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_MASK,
)
@Composable
private fun DarkThemePreview() {
    PokedexAppThemePreview(darkTheme = true) {
        val navController = rememberNavController()
        val entries = (0 until 4).map {
            PokemonEntry(
                "Pokemon $it",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$it.png",
                number = it,
            )
        }

        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .padding(32.dp)
        ) {
            for (index in entries.indices step 2) {
                PokedexRow(
                    first = entries[index],
                    last = if (index + 1 >= entries.size) null else entries[index + 1],
                    navController = navController,
                    viewModel = PokemonListViewModel(
                        getPokemonListUseCaseStub,
                    ),
                )
            }
        }
    }
}
