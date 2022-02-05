package dev.tonholo.study.pokedex.screens.pokemonList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tonholo.study.pokedex.data.model.PokemonEntry
import dev.tonholo.study.pokedex.usecases.GetPokemonListUseCase
import javax.inject.Inject

const val PAGE_SIZE = 20

@ExperimentalPagingApi
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    val getPokemonListUseCase: GetPokemonListUseCase,
) : ViewModel() {
    val loadingError = mutableStateOf("")
    val isSearching = mutableStateOf(false)
    val pokemonList = getPokemonListUseCase()

    private var cachedPokemonList = listOf<PokemonEntry>()

    fun filterPokemonList(query: String) {
//        val listToSearch = if (isSearching.value) {
//            cachedPokemonList
//        } else {
//            pokemonList.value
//        }
//
//        viewModelScope.launch(Dispatchers.Default) {
//            if (query.isBlank()) {
//                pokemonList.value = cachedPokemonList
//                isSearching.value = false
//                return@launch
//            }
//
//            val results = listToSearch.filter {
//                with(query.lowercase(Locale.getDefault()).trim()) {
//                    it.pokemonName.lowercase(Locale.getDefault()).contains(this)
//                            || it.number.toString() == this
//                }
//            }
//
//            if (!isSearching.value) {
//                cachedPokemonList = pokemonList.value
//                isSearching.value = true
//            }
//
//            pokemonList.value = results
//        }
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
