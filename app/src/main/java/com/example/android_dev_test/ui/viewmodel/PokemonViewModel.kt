package com.example.android_dev_test.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_dev_test.ui.PokemonDetailsState
import com.example.android_dev_test.ui.PokemonListState
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

    private val _pokemonList = MutableStateFlow<PokemonListState>(PokemonListState.Loading)
    val pokemonList: StateFlow<PokemonListState> = _pokemonList.asStateFlow()

    private val _pokemonDetails = MutableStateFlow<PokemonDetailsState>(PokemonDetailsState.Loading)
    val pokemonDetails: StateFlow<PokemonDetailsState> = _pokemonDetails.asStateFlow()

    // Store the full list of Pokémon
    private var fullPokemonList: List<PokemonResult> = emptyList()

    // Store the current search query
    private val _searchQuery = MutableStateFlow<String?>(null)
    val searchQuery: StateFlow<String?> = _searchQuery.asStateFlow()

    fun loadPokemonList(limit: Int = 10, offset: Int = 0) {
        viewModelScope.launch {
            _pokemonList.value = PokemonListState.Loading

            try {
                val response = repository.getPokemonList(limit, offset)
                fullPokemonList = response.results
                applySearchFilter()
            } catch (e: Exception) {
                _pokemonList.value = PokemonListState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun loadPokemonDetails(name: String) {
        viewModelScope.launch {
            _pokemonDetails.value = PokemonDetailsState.Loading
            try {
                val response = repository.getPokemonDetails(name)
                _pokemonDetails.value = PokemonDetailsState.Success(response)
            } catch (e: Exception) {
                _pokemonDetails.value = PokemonDetailsState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun searchPokemon(query: String) {
        _searchQuery.value = query
        applySearchFilter()
    }

    private fun applySearchFilter() {
        val query = _searchQuery.value
        if (query.isNullOrEmpty()) {
            // Show the full list if there's no search query
            _pokemonList.value = PokemonListState.Success(
                PokemonResponse(
                    fullPokemonList.size,
                    null,
                    null,
                    fullPokemonList
                )
            )
        } else {
            // Filter the list based on the search query
            val filteredList = fullPokemonList.filter { pokemon ->
                pokemon.name.contains(query, ignoreCase = true)
            }
            _pokemonList.value = PokemonListState.Success(
                PokemonResponse(
                    filteredList.size,
                    null,
                    null,
                    filteredList
                )
            )
        }
    }
}