package com.vancoding.pokemon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vancoding.pokemon.domain.model.PokemonDetails
import com.vancoding.pokemon.domain.usecases.GetPokemonDetailUseCase
import com.vancoding.pokemon.utils.NetworkResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
) : ViewModel() {

    private val _pokemonDetails = MutableStateFlow<NetworkResultState<PokemonDetails>>(NetworkResultState.Loading())
    val pokemonDetails: StateFlow<NetworkResultState<PokemonDetails>> = _pokemonDetails

    fun getPokemonDetails(id: Int) {
        viewModelScope.launch {
            getPokemonDetailUseCase(id).collect { result ->
                _pokemonDetails.value = result
            }
        }
    }
}