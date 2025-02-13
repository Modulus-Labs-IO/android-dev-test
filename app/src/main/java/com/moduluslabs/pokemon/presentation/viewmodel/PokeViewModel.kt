package com.moduluslabs.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moduluslabs.pokemon.domain.model.PokeResult
import com.moduluslabs.pokemon.domain.model.Pokemon
import com.moduluslabs.pokemon.domain.model.PokemonDetails
import com.moduluslabs.pokemon.domain.usecase.GetSavedPokemonUseCase
import com.moduluslabs.pokemon.domain.usecase.PokemonDetailUseCase
import com.moduluslabs.pokemon.domain.usecase.PokemonUseCase
import com.moduluslabs.pokemon.domain.usecase.SavePokemonUseCase
import com.moduluslabs.pokemon.utils.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokeViewModel @Inject constructor(
    private val pokemonUseCase: PokemonUseCase,
    private val pokemonDetailsUseCase: PokemonDetailUseCase,
    private val getSavedPokemonUseCase: GetSavedPokemonUseCase,
    private val savePokemonUseCase: SavePokemonUseCase
) : ViewModel() {

    private val _pokemonState = MutableStateFlow<ResourceState<Pokemon>>(ResourceState.Loading())
    val pokemonState: StateFlow<ResourceState<Pokemon>> = _pokemonState

    private val _pokemonDetailState = MutableStateFlow<ResourceState<PokemonDetails>>(ResourceState.Loading())
    val pokemonDetailState: StateFlow<ResourceState<PokemonDetails>> = _pokemonDetailState

    fun fetchPokemon(limit: String, offset: Int) {
        viewModelScope.launch {
            _pokemonState.value = ResourceState.Loading()

            try {
                val response = pokemonUseCase.invoke(limit, offset)
                _pokemonState.value = response
            } catch (e: Exception) {
                _pokemonState.value = ResourceState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun fetchPokemonDetails(pokemonName: String) {
        viewModelScope.launch {
            _pokemonDetailState.value = ResourceState.Loading()

            try {
                val response = pokemonDetailsUseCase.invoke(pokemonName)
                _pokemonDetailState.value = response
            } catch (e: Exception) {
                _pokemonDetailState.value = ResourceState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun savePokemon(pokeResult: PokeResult) = viewModelScope.launch {
        savePokemonUseCase.invoke(pokeResult)
    }

    fun getSavedPokemon() = flow{
        getSavedPokemonUseCase.invoke().collect{
            emit(it)
        }
    }
}
