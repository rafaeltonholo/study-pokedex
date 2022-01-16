package dev.tonholo.study.pokedex.screens.pokemonDetail.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.tonholo.study.pokedex.R
import dev.tonholo.study.pokedex.ui.theme.PokedexAppTheme

@Composable
fun PokemonDetailDataSection(
    pokemonWeight: Int,
    pokemonHeight: Int,
    sectionHeight: Dp = 80.dp,
) {
    val pokemonWeightInKg = remember {
        pokemonWeight / 10f
    }
    val pokemonHeightInMeters = remember {
        pokemonHeight / 10f
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PokemonDetailDataItem(
            value = pokemonHeightInMeters,
            unit = "m",
            icon = painterResource(id = R.drawable.ic_baseline_height_36),
            contentDescription = stringResource(id = R.string.pokemon_detail_height),
            modifier = Modifier.weight(1f),
        )
        Spacer(
            modifier = Modifier
                .size(width = 1.dp, height = sectionHeight)
                .background(MaterialTheme.colors.onSurface.copy(alpha = .7f))
        )
        PokemonDetailDataItem(
            value = pokemonWeightInKg,
            unit = "kg",
            icon = painterResource(id = R.drawable.ic_baseline_fitness_center_36),
            contentDescription = stringResource(id = R.string.pokemon_detail_weight),
            modifier = Modifier.weight(1f),
        )
    }

}

@Composable
private fun PokemonDetailDataItem(
    value: Float,
    unit: String,
    icon: Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colors.onSurface,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$value$unit",
            color = MaterialTheme.colors.onSurface,
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun LightThemePreview() {
    PokedexAppTheme(darkTheme = false) {
        PokemonDetailDataSection(
            pokemonWeight = 100,
            pokemonHeight = 1000,
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
        Box(modifier = Modifier.background(MaterialTheme.colors.background)) {
            PokemonDetailDataSection(
                pokemonWeight = 100,
                pokemonHeight = 1000,
            )
        }
    }
}
