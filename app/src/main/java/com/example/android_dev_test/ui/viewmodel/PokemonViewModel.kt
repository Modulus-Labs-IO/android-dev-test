package com.example.android_dev_test.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_dev_test.PokemonState
import com.example.android_dev_test.data.model.PokemonDetailsResponse
import com.example.android_dev_test.data.model.PokemonResponse
import com.example.android_dev_test.data.model.PokemonResult
import com.example.android_dev_test.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonList  = MutableStateFlow<PokemonState>(PokemonState.Loading)
    val pokemonList: StateFlow<PokemonState> = _pokemonList.asStateFlow()

    fun loadPokemonList(limit: Int = 20, offset: Int = 0) {
        viewModelScope.launch {
            _pokemonList.value = PokemonState.Loading

            try {
                val response = repository.getPokemonList(limit, offset)
                _pokemonList.value = PokemonState.Success(response)
            } catch (e: Exception) {
                _pokemonList.value = PokemonState.Error(e.message ?: "An error occurred")
            }
        }
    }


    private fun PokemonDetailsResponse.toPokemonResult(): PokemonResult {
        return PokemonResult(name, sprites.front_default ?: "")
    }

    fun searchPokemon(name: String) {
        viewModelScope.launch {
            _pokemonList.value = PokemonState.Loading
            try {
                val response = repository.getPokemonDetails(name)
                _pokemonList.value = PokemonState.Success(PokemonResponse(1, null, null, listOf(response.toPokemonResult())))
            } catch (e: Exception) {
                _pokemonList.value = PokemonState.Error(e.message ?: "An error occurred")
            }
        }
    }
}