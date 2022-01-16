package dev.tonholo.study.pokedex.screens.pokemonDetail.components

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tonholo.study.pokedex.R
import dev.tonholo.study.pokedex.data.remote.responses.PokemonStat
import dev.tonholo.study.pokedex.data.remote.responses.StatX
import dev.tonholo.study.pokedex.data.remote.responses.toAbbreviation
import dev.tonholo.study.pokedex.data.remote.responses.toColor
import dev.tonholo.study.pokedex.ui.theme.PokedexAppTheme

@Composable
fun PokemonDetailBaseStatsSection(
    stats: List<PokemonStat>,
    modifier: Modifier = Modifier,
    animationDelayPerItem: Int = 100,
) {
    val maxBaseStat = remember {
        stats.maxOf { it.baseStat }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 100.dp)
    ) {
        Text(
            text = stringResource(id = R.string.pokemon_base_stats),
            fontSize = 20.sp,
            color = MaterialTheme.colors.onSurface,
        )
        Spacer(modifier = Modifier.height(4.dp))

        for ((index, stat) in stats.withIndex()) {
            PokemonStat(
                name = stat.toAbbreviation(),
                value = stat.baseStat,
                maxValue = maxBaseStat,
                color = stat.toColor(),
                animationDelay = index * animationDelayPerItem,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun PokemonStat(
    name: String,
    value: Int,
    maxValue: Int,
    color: Color,
    animationDelay: Int,
) {
    if (value <= 0) return

    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val currentPercent = animateFloatAsState(
        targetValue = if (animationPlayed) value.toFloat() / maxValue.toFloat() else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = animationDelay,
        )
    )

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.15f), CircleShape)
            .fillMaxWidth()
            .height(36.dp),
    ) {
        Row(
            modifier = Modifier
                .background(color, CircleShape)
                .fillMaxWidth(currentPercent.value)
                .fillMaxHeight()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val statBarTextColor = Color.Black
            val statBarFontWeight = FontWeight.Bold
            Text(
                text = name,
                color = statBarTextColor,
                fontWeight = statBarFontWeight,
            )
            Text(
                text = value.toString(),
                color = statBarTextColor,
                fontWeight = statBarFontWeight,
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
        PokemonDetailBaseStatsSection(
            stats = previewStats,
            modifier = Modifier.padding(8.dp),
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
            PokemonDetailBaseStatsSection(
                stats = previewStats,
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

private val previewStats = listOf(
    PokemonStat(
        baseStat = 100,
        effort = 1,
        stat = StatX(
            name = "hp",
            url = "",
        )
    ),
    PokemonStat(
        baseStat = 50,
        effort = 1,
        stat = StatX(
            name = "attack",
            url = "",
        )
    ),
    PokemonStat(
        baseStat = 75,
        effort = 1,
        stat = StatX(
            name = "defense",
            url = "",
        )
    ),
    PokemonStat(
        baseStat = 150,
        effort = 1,
        stat = StatX(
            name = "special-attack",
            url = "",
        )
    ),
    PokemonStat(
        baseStat = 80,
        effort = 1,
        stat = StatX(
            name = "special-defense",
            url = "",
        )
    ),
    PokemonStat(
        baseStat = 30,
        effort = 1,
        stat = StatX(
            name = "speed",
            url = "",
        )
    ),
    PokemonStat(
        baseStat = 0,
        effort = 1,
        stat = StatX(
            name = "other",
            url = "",
        )
    ),
)
