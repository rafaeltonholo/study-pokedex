package dev.tonholo.study.pokedex.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import dev.tonholo.study.pokedex.ui.theme.state.ThemeState
import dev.tonholo.study.pokedex.ui.theme.state.ThemeStateHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val handler: ThemeStateHandler,
) : ViewModel() {
    val theme: StateFlow<ThemeState> = handler.theme

    fun onSwitchThemeRequest() {
        handler.switchThemeRequest(theme.value)
    }

}
