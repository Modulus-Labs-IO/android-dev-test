package com.example.pokemonapp.features.pokemon.domain.repository

import androidx.paging.PagingData
import com.example.pokemonapp.core.util.DataError
import com.example.pokemonapp.core.util.Result
import com.example.pokemonapp.features.pokemon.data.datasource.remote.dto.PokemonDetailDto
import com.example.pokemonapp.features.pokemon.domain.model.PokemonListItem
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun getPokemonList(): Flow<PagingData<PokemonListItem>>

    suspend fun searchPokemon(
        name: String
    ): Flow<List<PokemonListItem>>

}