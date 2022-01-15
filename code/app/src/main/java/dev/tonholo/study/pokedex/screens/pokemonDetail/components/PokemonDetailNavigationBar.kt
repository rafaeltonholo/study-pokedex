package dev.tonholo.study.pokedex.screens.pokemonDetail.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.tonholo.study.pokedex.R
import dev.tonholo.study.pokedex.ui.theme.PokedexAppTheme

@Composable
fun PokemonDetailNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Image(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.pokemon_detail_navigate_back),
                modifier = Modifier
                    .size(36.dp)
                    .offset(x = 16.dp, y = 16.dp),
                colorFilter = ColorFilter.tint(Color.White)
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
        val navController = rememberNavController()
        PokemonDetailNavigationBar(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
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
        PokemonDetailNavigationBar(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
        )
    }
}
