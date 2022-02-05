package dev.tonholo.study.pokedex.screens.pokemonList.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import dev.tonholo.study.pokedex.screens.pokemonList.PokemonListViewModel

@ExperimentalPagingApi
@ExperimentalCoilApi
@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel(),
) {
    val lazyPagingItems = viewModel.pokemonList.collectAsLazyPagingItems()
    val loadError by remember { viewModel.loadingError }
    val isSearching by remember { viewModel.isSearching }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        for (index in 0 until lazyPagingItems.itemCount step 2) {
            item {
                lazyPagingItems[index]?.let { first ->
                    PokedexRow(
                        first = first,
                        last = lazyPagingItems[index + 1],
                        navController = navController,
                    )
                }
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
//        if (isLoading) {
//            CircularProgressIndicator(
//                color = MaterialTheme.colors.primary,
//            )
//        }

        if (loadError.isNotBlank()) {
            Text(
                loadError
            )
        }
    }
}
