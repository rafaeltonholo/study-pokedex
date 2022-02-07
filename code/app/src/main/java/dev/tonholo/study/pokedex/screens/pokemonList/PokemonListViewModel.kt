package dev.tonholo.study.pokedex.screens.pokemonList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tonholo.study.pokedex.data.model.PokemonEntry
import dev.tonholo.study.pokedex.usecases.GetPokemonListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

const val PAGE_SIZE = 20

@ExperimentalPagingApi
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    getPokemonListUseCase: GetPokemonListUseCase,
) : ViewModel() {
    val isLoading = mutableStateOf(false)
    val loadingError = mutableStateOf("")
    val isSearching = mutableStateOf(false)
    val pokemonList = getPokemonListUseCase()

    var currentSearchingList = mutableStateOf(listOf<PokemonEntry>())
    private var cachedPokemonList = listOf<PokemonEntry>()

    fun handleLoadState(lazyPagingItems: LazyPagingItems<PokemonEntry>) {
        if (!isSearching.value) currentSearchingList.value = lazyPagingItems.itemSnapshotList.items

        val loadState = lazyPagingItems.loadState
        when  {
            loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading ->
                isLoading.value = true
            loadState.refresh is LoadState.Error -> {
                isLoading.value = false
                val loadStateError = loadState.refresh as LoadState.Error
                loadingError.value = loadStateError.error.localizedMessage ?: "Unhandled error"
            }
            loadState.append is LoadState.Error -> {
                isLoading.value = false
                val loadStateError = loadState.append as LoadState.Error
                loadingError.value = loadStateError.error.localizedMessage ?: "Unhandled error"
            }
            loadState.refresh is LoadState.NotLoading && loadState.append is LoadState.NotLoading ->
                isLoading.value = false
        }
    }

    fun filterPokemonList(query: String) {
        val listToSearch = if (isSearching.value) {
            cachedPokemonList
        } else {
            currentSearchingList.value
        }

        viewModelScope.launch(Dispatchers.Default) {
            if (query.isBlank()) {
                currentSearchingList.value = cachedPokemonList
                isSearching.value = false
                return@launch
            }

            val results = listToSearch.filter {
                with(query.lowercase(Locale.getDefault()).trim()) {
                    it.pokemonName.lowercase(Locale.getDefault()).contains(this)
                            || it.number.toString() == this
                }
            }

            if (!isSearching.value) {
                cachedPokemonList = currentSearchingList.value
                isSearching.value = true
            }

            currentSearchingList.value = results
        }
    }

    fun getDominantColor(drawable: Drawable): Color {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true)
        val colorRgb = newBitmap.getPixel(0, 0)

        newBitmap.recycle()
        bitmap.recycle()

        return Color(colorRgb)
    }
}
