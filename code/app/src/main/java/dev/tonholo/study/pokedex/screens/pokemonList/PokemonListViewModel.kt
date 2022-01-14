package dev.tonholo.study.pokedex.screens.pokemonList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tonholo.study.pokedex.data.model.PokemonEntry
import dev.tonholo.study.pokedex.usecases.GetPokemonListUseCase
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

const val PAGE_SIZE = 20

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    val getPokemonListUseCase: GetPokemonListUseCase,
) : ViewModel() {
    private var currentPage = 0

    val pokemonList = mutableStateOf<List<PokemonEntry>>(listOf())
    val loadingError = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val isEndReached = mutableStateOf(false)

    init {
        loadPokemonListPaginated()
    }

    fun loadPokemonListPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            val params = GetPokemonListUseCase.Params(
                limit = PAGE_SIZE,
                offset = currentPage * PAGE_SIZE,
            )

            when (val result = getPokemonListUseCase(params)) {
                is GetPokemonListUseCase.Result.Success -> {
                    with(result.data) {
                        isEndReached.value = currentPage * PAGE_SIZE >= count
                        val pokedexEntries = result.data.results.map { entry ->
                            val number = if (entry.url.endsWith("/")) {
                                entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                            } else {
                                entry.url.takeLastWhile { it.isDigit() }
                            }

                            val url =
                                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"

                            PokemonEntry(
                                pokemonName = entry.name.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                                },
                                imageUrl = url,
                                number = number.toInt()
                            )
                        }
                        pokemonList.value += pokedexEntries
                    }
                    isLoading.value = false
                    currentPage++
                }
                is GetPokemonListUseCase.Result.Failure -> {
                    loadingError.value = "${result.message}\n${result.exception?.message}"
                    isLoading.value = false
                }
            }
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
