package dev.tonholo.study.pokedex.screens.pokemonList.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import dev.tonholo.study.pokedex.screens.pokemonList.PokemonListViewModel

@ExperimentalCoilApi
@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel(),
) {
    val pokemonList by remember { viewModel.pokemonList }
    val isEndReached by remember { viewModel.isEndReached }
    val loadError by remember { viewModel.loadingError }
    val isLoading by remember { viewModel.isLoading }
    val isSearching by remember { viewModel.isSearching }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        with(pokemonList) {
            val itemCount = if (size % 2 == 0) {
                size / 2
            } else {
                size / 2 + 1
            }
            items(itemCount) {
                if (it >= itemCount - 1 && !isEndReached && !isLoading && !isSearching) {
                    viewModel.loadPokemonListPaginated()
                }
                PokedexRow(
                    rowIndex = it,
                    entries = this@with,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }

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
