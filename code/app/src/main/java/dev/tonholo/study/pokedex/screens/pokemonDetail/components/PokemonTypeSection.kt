package dev.tonholo.study.pokedex.screens.pokemonDetail.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tonholo.study.pokedex.data.remote.responses.Type
import dev.tonholo.study.pokedex.data.remote.responses.TypeX
import dev.tonholo.study.pokedex.data.remote.responses.toColor
import dev.tonholo.study.pokedex.ui.theme.PokedexAppTheme
import java.util.*

@Composable
fun PokemonTypeSection(
    types: List<Type>,
) {
    Row(
        modifier = Modifier
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (type in types) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .background(type.toColor(), CircleShape)
                    .height(36.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = type.type.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                    },
                    color = Color.White,
                    fontSize = 18.sp,
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun LightThemePreview() {
    PokedexAppTheme(darkTheme = false) {
        PokemonTypeSection(
            types = previewTypes,
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
            PokemonTypeSection(
                types = previewTypes,
            )
        }
    }
}

private val previewTypes = listOf(
    Type(
        slot = 0,
        type = TypeX(
            name = "electric",
            url = "https://pokeapi.co/api/v2/type/13/"
        ),
    ),
    Type(
        slot = 0,
        type = TypeX(
            name = "steel",
            url = "https://pokeapi.co/api/v2/type/9/"
        ),
    ),
)
