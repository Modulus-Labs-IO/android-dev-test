package com.vancoding.pokemon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vancoding.pokemon.domain.model.Pokemon
import com.vancoding.pokemon.domain.usecases.GetPokemonListUseCase
import com.vancoding.pokemon.domain.usecases.SearchPokemonUseCase
import com.vancoding.pokemon.utils.Constants.CURRENT_PAGE
import com.vancoding.pokemon.utils.Constants.POKEMON_LIST_LIMIT
import com.vancoding.pokemon.utils.NetworkResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val searchPokemonUseCase: SearchPokemonUseCase,
) : ViewModel() {

    private val _pokemonList = MutableStateFlow<NetworkResultState<List<Pokemon>>>(NetworkResultState.Loading())
    val pokemonList: StateFlow<NetworkResultState<List<Pokemon>>> = _pokemonList

    private val _isSearchMode = MutableStateFlow(false)
    val isSearchMode: StateFlow<Boolean> = _isSearchMode

    private var currentPage = CURRENT_PAGE
    private val pageSize = POKEMON_LIST_LIMIT
    private var currentSearchQuery = ""

    init {
        loadPokemonList()
    }

    private fun loadPokemonList() {
        if (_isSearchMode.value) return

        viewModelScope.launch {
            getPokemonListUseCase(pageSize, currentPage).collect { result ->
                _pokemonList.value = result
            }
        }
    }

    fun loadNextPage() {
        if (_isSearchMode.value) return

        currentPage++
        loadPokemonList()
    }

    fun refreshList() {
        _isSearchMode.value = false
        currentSearchQuery = ""
        currentPage = CURRENT_PAGE
        loadPokemonList()
    }

    fun searchPokemon(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _isSearchMode.value = false
                currentSearchQuery = ""
                loadPokemonList()
                return@launch
            }

            _isSearchMode.value = true
            currentSearchQuery = query

            _pokemonList.value = NetworkResultState.Loading()
            searchPokemonUseCase(query).collect { result ->
                if (_isSearchMode.value && currentSearchQuery == query) {
                    _pokemonList.value = result
                }
            }
        }
    }
}