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
import dev.tonholo.study.pokedex.util.toTitleCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private const val PAGE_SIZE = 20
private const val TAG = "PokemonListViewModel"

@FlowPreview
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
        Log.d(TAG, "loadPokemonListPaginated() called")
        Log.d(TAG, "loadPokemonListPaginated: currentPage = $currentPage")
        Log.d(TAG, "loadPokemonListPaginated: pokemonList.size = ${pokemonList.value.size}")
        Log.d(TAG, "loadPokemonListPaginated: isEndReached = $isEndReached")
        Log.d(TAG, "loadPokemonListPaginated: isSearching = $isSearching")

        val offset = PAGE_SIZE
        val cursor = currentPage * PAGE_SIZE
        viewModelScope.launch {
            getPokemonListFromDatabaseUseCase(GetPokemonListFromDatabaseUseCase.Params(
                cursor = cursor,
                offset = offset,
            ))
                .map(::mapFromDatabase)
                .map(::mapFromRemote)
                .map(::cacheRemoteResponse)
                .distinctUntilChanged()
                .collectLatest { resource ->
                    Log.d(TAG, "collectLatest() called with: resource = $resource")
                    Log.d(TAG, "collectLatest: pokemonList.size = ${pokemonList.value.size}")
                    Log.d(TAG, "collectLatest: currentPage = $currentPage")

                    when (resource) {
                        is Resource.Error -> {
                            loadingError.value = "${resource.message}\n${resource.throwable?.localizedMessage}"
                            isLoading.value = false
                        }
                        is Resource.Loading -> isLoading.value = true
                        is Resource.Success -> {
                            if (resource.data.isNotEmpty()) {
                                pokemonList.value += resource.data
                                isLoading.value = false
                                currentPage++
                                Log.d(TAG, "collectLatest: pokemonList.size = ${pokemonList.value.size}")
                            }
                        }
                    }
                }
        }
    }

    private fun mapFromDatabase(result: GetPokemonListFromDatabaseUseCase.Result): Resource<List<PokemonEntry>> {
        Log.d(TAG, "mapFromDatabase() called with: result = $result")
        return when (result) {
            is GetPokemonListFromDatabaseUseCase.Result.Failure ->
                Resource.Error(result.exception, result.message)
            GetPokemonListFromDatabaseUseCase.Result.IsLoading -> {
                Resource.Loading()
            }
            is GetPokemonListFromDatabaseUseCase.Result.Success -> {
                with(result.data.results) {
                    if (isNotEmpty()) {
                        Resource.Success(
                            map { pokemonListResult ->
                                PokemonEntry(
                                    pokemonName = pokemonListResult.name,
                                    imageUrl = pokemonListResult.url,
                                    number = pokemonListResult.number
                                        ?: throw Exception("Pokemon number must not  be null coming from database"),
                                )
                            }
                        )
                    } else {
                        Resource.Success(emptyList())
                    }
                }
            }
        }
    }

    private suspend fun mapFromRemote(
        resource: Resource<List<PokemonEntry>>
    ): Pair<Boolean, Resource<List<PokemonEntry>>> {
        Log.d(TAG, "mapFromRemote() called with: resource = $resource")
        return if (resource is Resource.Success && resource.data.isNullOrEmpty()) {
            val params = GetPokemonListFromRemoteUseCase.Params(
                limit = PAGE_SIZE,
                offset = currentPage * PAGE_SIZE,
            )
            Log.d(TAG, "mapFromRemote: requesting from remote with: params = $params")
            when (val result = getPokemonListFromRemoteUseCase(params)) {
                is GetPokemonListFromRemoteUseCase.Result.Failure ->
                    (false to Resource.Error(result.exception, result.message))
                is GetPokemonListFromRemoteUseCase.Result.Success -> {
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
                                pokemonName = entry.name.toTitleCase(),
                                imageUrl = url,
                                number = number.toInt()
                            )
                        }

                        (true to Resource.Success(pokedexEntries))
                    }
                }
            }
        } else {
            Log.d(TAG, "mapFromRemote: emitting database result")
            (false to resource)
        }
    }

    private suspend fun cacheRemoteResponse(
        pair: Pair<Boolean, Resource<List<PokemonEntry>>>,
    ): Resource<List<PokemonEntry>> {
        Log.d(TAG, "cacheRemoteResponse() called with: pair = $pair")
        val (shouldCacheData, resource) = pair
        return if (resource is Resource.Success && shouldCacheData) {
            when (val result = cachePokemonListUseCase(resource.data)) {
                is CachePokemonListUseCase.Result.Failure -> {
                    Resource.Error(
                        throwable = result.exception,
                        message = result.message,
                        data = resource.data,
                    )
                }
                is CachePokemonListUseCase.Result.Success -> {
                    return Resource.Success(emptyList())
                }
            }
        } else {
            resource
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
