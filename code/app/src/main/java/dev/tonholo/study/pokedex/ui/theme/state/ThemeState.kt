package dev.tonholo.study.pokedex.ui.theme.state

sealed class ThemeState {
    object Dark : ThemeState()
    object Light : ThemeState()
    object Auto: ThemeState()
}
