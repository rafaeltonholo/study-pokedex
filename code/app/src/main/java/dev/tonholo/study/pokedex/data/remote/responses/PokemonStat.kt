package dev.tonholo.study.pokedex.data.remote.responses


import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.SerializedName
import dev.tonholo.study.pokedex.ui.theme.*

data class PokemonStat(
    @SerializedName("base_stat")
    val baseStat: Int,
    val effort: Int,
    val stat: StatX,
)

fun PokemonStat.toAbbreviation() = when(stat.name.lowercase()) {
    "hp" -> "HP"
    "attack" -> "Atk"
    "defense" -> "Def"
    "special-attack" -> "SpAtk"
    "special-defense" -> "SpDef"
    "speed" -> "Spd"
    else -> ""
}

fun PokemonStat.toColor() = when(stat.name.lowercase()) {
    "hp" -> HPColor
    "attack" -> AtkColor
    "defense" -> DefColor
    "special-attack" -> SpAtkColor
    "special-defense" -> SpDefColor
    "speed" -> SpdColor
    else -> Color.White
}
