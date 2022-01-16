package dev.tonholo.study.pokedex.data.remote.responses

import androidx.compose.ui.graphics.Color
import dev.tonholo.study.pokedex.ui.theme.*


data class Type(
    val slot: Int,
    val type: TypeX,
)

fun Type.toColor(): Color = when (type.name.lowercase()) {
    "normal" -> TypeNormal
    "fire" -> TypeFire
    "water" -> TypeWater
    "electric" -> TypeElectric
    "grass" -> TypeGrass
    "ice" -> TypeIce
    "fighting" -> TypeFighting
    "poison" -> TypePoison
    "ground" -> TypeGround
    "flying" -> TypeFlying
    "psychic" -> TypePsychic
    "bug" -> TypeBug
    "rock" -> TypeRock
    "ghost" -> TypeGhost
    "dragon" -> TypeDragon
    "dark" -> TypeDark
    "steel" -> TypeSteel
    "fairy" -> TypeFairy
    else -> Color.Black
}
