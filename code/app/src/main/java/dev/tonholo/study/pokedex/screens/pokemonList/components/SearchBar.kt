package dev.tonholo.study.pokedex.screens.pokemonList.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.tonholo.study.pokedex.ui.theme.PokedexAppTheme

@Composable
fun SearchBar(
    hint: String,
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {},
) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        val horizontalPadding = 20.dp
        val verticalPadding = 12.dp
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(text)
            },
            maxLines = 1,
            textStyle = TextStyle(MaterialTheme.colors.onSurface),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(MaterialTheme.colors.surface)
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
                .onFocusChanged {
                    isHintDisplayed = !it.isFocused
                }
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.35f),
                modifier = Modifier
                    .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            )
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun LightThemePreview() {
    PokedexAppTheme {
        SearchBar(
            hint = "Testing Light theme...",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_MASK,
)
@Composable
fun DarkThemePreview() {
    PokedexAppTheme(darkTheme = true) {
        SearchBar(
            hint = "Testing Dark theme...",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}
