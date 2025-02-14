package com.example.pokemonapp.features.pokemon.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.pokemonapp.core.util.NetworkUtil
import com.example.pokemonapp.features.pokemon.data.datasource.local.database.PokemonDao
import com.example.pokemonapp.features.pokemon.data.mapper.mapToDomainModel
import com.example.pokemonapp.features.pokemon.domain.repository.PokemonRepository
import com.example.pokemonapp.features.pokemon.presentation.list.PokemonLisAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val repository: PokemonRepository,
    private val dao: PokemonDao,
    private val networkUtil: NetworkUtil
) : ViewModel() {

    // Expose the network connectivity status as a StateFlow
    private val isConnected: StateFlow<Boolean> = networkUtil.observeNetworkConnectivity().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Stop after 5 seconds of inactivity
            initialValue = networkUtil.isConnected() // Initial value
        )

    private val _searchText = MutableStateFlow("")

    val pokemonPagingData =
        _searchText.asStateFlow()
            .debounce(500)
            .combine(isConnected) { searchText, isConnected ->
                if (isConnected) {
                    repository.getPokemonList().map {
                        it.filter { data -> data.name.contains(searchText, ignoreCase = true) }
                    }.cachedIn(viewModelScope)
                } else {
                    Pager(
                        config = PagingConfig(
                            pageSize = 10,
                            enablePlaceholders = false
                        ),
                        pagingSourceFactory = { dao.getPagedPokemon() }
                    ).flow.map {
                        it.map { pagingData -> pagingData.mapToDomainModel() }.filter { data -> data.name.contains(searchText, ignoreCase = true) }
                    }.cachedIn(viewModelScope)
                }
            }.flatMapLatest { pagingData ->
                pagingData
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                PagingData.empty()
            )

    fun onPokemonListAction(action: PokemonLisAction) {
        when (action) {
            is PokemonLisAction.OnSearchPokemon -> {
                _searchText.update {
                    action.keyword
                }
            }
        }
    }

}