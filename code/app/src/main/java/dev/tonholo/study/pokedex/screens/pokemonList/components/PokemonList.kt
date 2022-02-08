package dev.tonholo.study.pokedex.screens.pokemonList.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.tonholo.study.pokedex.screens.destinations.PokemonDetailScreenDestination
import dev.tonholo.study.pokedex.screens.pokemonList.PokemonListViewModel

@ExperimentalPagingApi
@ExperimentalCoilApi
@Composable
fun PokemonList(
    navigator: DestinationsNavigator,
    viewModel: PokemonListViewModel = hiltViewModel(),
) {
    val lazyPagingItems = viewModel.pokemonList.collectAsLazyPagingItems()
    val loadError by remember { viewModel.loadingError }
    val isSearching by remember { viewModel.isSearching }
    val isLoading by remember { viewModel.isLoading }
    val currentSearchingList by remember { viewModel.currentSearchingList }
    val listState = rememberLazyListState()

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        state = listState,
    ) {
        for (index in 0 until lazyPagingItems.itemCount step 2) {
            item {
                val (firstEntry, secondEntry) = if (isSearching) {
                    currentSearchingList.elementAtOrNull(index) to currentSearchingList.elementAtOrNull(index + 1)
                } else {
                    lazyPagingItems[index] to lazyPagingItems[index + 1]
                }

                firstEntry?.let { first ->
                    PokedexRow(
                        first = first,
                        last = secondEntry,
                    ) { entry, dominantColor ->
                        navigator.navigate(PokemonDetailScreenDestination(dominantColor, entry.pokemonName))
                    }
                }
            }
        }
    }

    viewModel.handleLoadState(lazyPagingItems)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
            )
        }

        if (loadError.isNotBlank()) {
            Text(
                loadError
            )
        }
    }
}
