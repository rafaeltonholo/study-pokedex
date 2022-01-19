package dev.tonholo.study.pokedex.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import dev.tonholo.study.pokedex.ui.theme.state.ThemeState
import dev.tonholo.study.pokedex.ui.theme.viewModel.ThemeViewModel

private val DarkColorPalette = darkColors(
    primary = Color.Yellow,
    background = DarkGrey,
    onBackground = Color.White,
    surface = Color(0xFF303030),
    onSurface = Color.White
)

private val LightColorPalette = lightColors(
    primary = Color.Blue,
    background = LightBlue,
    onBackground = DarkGrey,
    surface = Color.White,
    onSurface = DarkGrey
)

@Composable
fun PokedexAppTheme(
    themeViewModel: ThemeViewModel = hiltViewModel(),
    content: @Composable() () -> Unit,
) {
    val theme by themeViewModel.theme.collectAsState()
    val colors = when (theme) {
        ThemeState.Dark -> DarkColorPalette
        ThemeState.Light -> LightColorPalette
        else -> if (isSystemInDarkTheme()) {
            DarkColorPalette
        } else {
            LightColorPalette
        }
    }

    Theme(
        colors = colors,
        content = content
    )
}

@Composable
fun PokedexAppThemePreview(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit,
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    Theme(
        colors = colors,
        content = content
    )
}

@Composable
private fun Theme(
    colors: Colors,
    content: @Composable() () -> Unit,
) = MaterialTheme(
    colors = colors,
    typography = Typography,
    shapes = Shapes,
    content = content
)
