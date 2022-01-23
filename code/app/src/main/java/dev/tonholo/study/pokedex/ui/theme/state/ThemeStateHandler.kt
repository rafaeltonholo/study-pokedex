package dev.tonholo.study.pokedex.ui.theme.state

import dev.tonholo.study.pokedex.data.dataStore.DataStoreManager
import dev.tonholo.study.pokedex.data.dataStore.extensions.getTheme
import dev.tonholo.study.pokedex.data.dataStore.extensions.setTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface ThemeStateHandler {
    val theme: StateFlow<ThemeState>
    suspend fun retrieveFromDataStore()
    suspend fun switchThemeRequest(currentTheme: ThemeState)
}

class ThemeStateHandlerImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager,
): ThemeStateHandler {
    private var _theme = MutableStateFlow<ThemeState>(ThemeState.Auto)
    override val theme: StateFlow<ThemeState> = _theme

    override suspend fun retrieveFromDataStore() {
        dataStoreManager.getTheme {
            _theme.value = this
        }
    }

    override suspend fun switchThemeRequest(currentTheme: ThemeState) {
        when (currentTheme) {
            is ThemeState.Auto -> _theme.value = ThemeState.Dark
            is ThemeState.Dark -> _theme.value = ThemeState.Light
            is ThemeState.Light -> _theme.value = ThemeState.Auto
        }

        dataStoreManager.setTheme(_theme.value)
    }
}
