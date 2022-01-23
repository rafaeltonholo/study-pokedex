package dev.tonholo.study.pokedex.data.dataStore.extensions

import androidx.datastore.preferences.core.intPreferencesKey
import dev.tonholo.study.pokedex.data.dataStore.DataStoreManager
import dev.tonholo.study.pokedex.ui.theme.state.ThemeState
import dev.tonholo.study.pokedex.ui.theme.state.toThemeState

private const val KEY_NAME = "theme_state"

suspend fun DataStoreManager.setTheme(value: ThemeState) {
    val key = intPreferencesKey(KEY_NAME)
    store(key, value.toInt())
}

suspend fun DataStoreManager.getTheme(onReadFinished: ThemeState.() -> Unit) {
    val key = intPreferencesKey(KEY_NAME)
    read(key) {
        onReadFinished(toThemeState())
    }
}
