package dev.tonholo.study.pokedex.screens.pokemonList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tonholo.study.pokedex.data.model.PokemonEntry
import dev.tonholo.study.pokedex.usecases.CachePokemonListUseCase
import dev.tonholo.study.pokedex.usecases.GetPokemonListFromDatabaseUseCase
import dev.tonholo.study.pokedex.usecases.GetPokemonListFromRemoteUseCase
import dev.tonholo.study.pokedex.util.Resource
import dev.tonholo.study.pokedex.util.networkBoundResource
import dev.tonholo.study.pokedex.util.toTitleCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

const val PAGE_SIZE = 20

private const val TAG = "PokemonListViewModel"

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    val getPokemonListFromRemoteUseCase: GetPokemonListFromRemoteUseCase,
    val getPokemonListFromDatabaseUseCase: GetPokemonListFromDatabaseUseCase,
    val cachePokemonListUseCase: CachePokemonListUseCase,
) : ViewModel() {
    private var currentPage = 0

    val pokemonList = mutableStateOf<List<PokemonEntry>>(listOf())
    val loadingError = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val isEndReached = mutableStateOf(false)
    val isSearching = mutableStateOf(false)

    private var cachedPokemonList = listOf<PokemonEntry>()

    init {
        loadPokemonListPaginated()
    }

    fun loadPokemonListPaginated() {
        val offset = PAGE_SIZE
        val cursor = currentPage * PAGE_SIZE
        Log.d(TAG, "loadPokemonListPaginated: currentPage = $currentPage")
        Log.d(TAG, "loadPokemonListPaginated: pokemonList.size = ${pokemonList.value.size}")
        Log.d(TAG, "loadPokemonListPaginated: isEndReached = $isEndReached")

        viewModelScope.launch {
            networkBoundResource(
                query = {
                    val params = GetPokemonListFromDatabaseUseCase.Params(
                        cursor = cursor,
                        offset = offset,
                    )
                    Log.d(TAG, "query: requesting from db with: params = $params")
                    getPokemonListFromDatabaseUseCase(params)
                },
                shouldFetch = { pokemonList ->
                    pokemonList.results.isEmpty()
                },
                fetch = {
                    val params = GetPokemonListFromRemoteUseCase.Params(
                        limit = offset,
                        offset = cursor,
                    )
                    Log.d(TAG, "fetch: requesting from remote with: params = $params")
                    getPokemonListFromRemoteUseCase(params)
                },
                saveFetchResult = { result ->
                    Log.d(TAG, "saveFetchResult: called with: result = $result")
                    when (result) {
                        is GetPokemonListFromRemoteUseCase.Result.Failure ->
                            throw Exception(result.message, result.exception)
                        is GetPokemonListFromRemoteUseCase.Result.Success -> {
                            val pokedexEntries = result.data.results.map { entry ->
                                val number = if (entry.url.endsWith("/")) {
                                    entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                                } else {
                                    entry.url.takeLastWhile { it.isDigit() }
                                }
                                val url =
                                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                                PokemonEntry(
                                    pokemonName = entry.name.toTitleCase(),
                                    imageUrl = url,
                                    number = number.toInt()
                                )
                            }
                            cachePokemonListUseCase(pokedexEntries)
                        }
                    }
                },
                onFetchSuccess = {
                    Log.d(TAG, "onFetchSuccess: result = $it")
                    when (it) {
                        is GetPokemonListFromRemoteUseCase.Result.Failure -> throw Exception(it.message, it.exception)
                        is GetPokemonListFromRemoteUseCase.Result.Success -> with(it.data) {
                            isEndReached.value = currentPage * PAGE_SIZE >= count
                        }
                    }
                }
            )
                .collectLatest { resource ->
                    Log.d(TAG, "collectLatest() called with: resource = $resource")
                    Log.d(TAG, "collectLatest: pokemonList.size = ${pokemonList.value.size}")
                    Log.d(TAG, "collectLatest: currentPage = $currentPage")

                    when (resource) {
                        is Resource.Error -> {
                            isLoading.value = false
                            loadingError.value = "${resource.message}\n${resource.throwable?.localizedMessage}"
                        }
                        is Resource.Loading -> isLoading.value = true
                        is Resource.Success -> {
                            isLoading.value = false

                            resource.data.run {
                                if (!pokemonList.value.any { it.number == results.first().number }
                                    && !pokemonList.value.any { it.number == results.last().number }) {
                                    pokemonList.value += results.map { pokemonListResult ->
                                        PokemonEntry(
                                            pokemonName = pokemonListResult.name,
                                            imageUrl = pokemonListResult.url,
                                            number = pokemonListResult.number
                                                ?: throw Exception("Pokemon number must not  be null coming from database"),
                                        )
                                    }
                                    currentPage++
                                    Log.d(TAG, "collectLatest: pokemonList.size = ${pokemonList.value.size}")
                                }
                            }

                        }
                    }
                }
        }
    }

    fun filterPokemonList(query: String) {
        val listToSearch = if (isSearching.value) {
            cachedPokemonList
        } else {
            pokemonList.value
        }

        viewModelScope.launch(Dispatchers.Default) {
            if (query.isBlank()) {
                pokemonList.value = cachedPokemonList
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
                cachedPokemonList = pokemonList.value
                isSearching.value = true
            }

            pokemonList.value = results
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
