package dev.tonholo.study.pokedex.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tonholo.study.pokedex.ui.theme.state.ThemeState
import dev.tonholo.study.pokedex.ui.theme.state.ThemeStateHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val handler: ThemeStateHandler,
) : ViewModel() {
    val theme: StateFlow<ThemeState> = handler.theme
    private val job: CoroutineContext = Dispatchers.IO + Job()

    init {
        viewModelScope.launch(job) {
            handler.retrieveFromDataStore()
        }
    }

    fun onSwitchThemeRequest() {
        viewModelScope.launch(job) {
            handler.switchThemeRequest(theme.value)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
