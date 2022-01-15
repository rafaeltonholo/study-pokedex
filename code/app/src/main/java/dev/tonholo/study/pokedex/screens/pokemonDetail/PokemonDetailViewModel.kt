package dev.tonholo.study.pokedex.screens.pokemonDetail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tonholo.study.pokedex.usecases.GetPokemonDetailUseCase
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
) : ViewModel() {
    suspend fun getPokemonDetail(pokemonName: String) = getPokemonDetailUseCase(pokemonName)
}
