package dev.tonholo.study.pokedex.ui.theme.state

sealed class ThemeState {
    object Dark : ThemeState()
    object Light : ThemeState()
    object Auto: ThemeState()

    fun toInt() = when (this) {
        is Auto -> 0
        is Dark -> 1
        is Light -> 2
    }
}

fun Int.toThemeState() = when (this) {
    1 -> ThemeState.Dark
    2 -> ThemeState.Light
    else -> ThemeState.Auto
}
