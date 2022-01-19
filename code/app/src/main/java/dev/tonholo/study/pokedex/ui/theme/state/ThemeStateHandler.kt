package dev.tonholo.study.pokedex.ui.theme.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface ThemeStateHandler {
    val theme: StateFlow<ThemeState>
    fun switchThemeRequest(currentTheme: ThemeState)
}

class ThemeStateHandlerImpl @Inject constructor(): ThemeStateHandler {
    private var _theme = MutableStateFlow<ThemeState>(ThemeState.Auto)
    override val theme: StateFlow<ThemeState> = _theme

    override fun switchThemeRequest(currentTheme: ThemeState) {
        when (currentTheme) {
            is ThemeState.Auto -> _theme.value = ThemeState.Dark
            is ThemeState.Dark -> _theme.value = ThemeState.Light
            is ThemeState.Light -> _theme.value = ThemeState.Auto
        }
    }
}
