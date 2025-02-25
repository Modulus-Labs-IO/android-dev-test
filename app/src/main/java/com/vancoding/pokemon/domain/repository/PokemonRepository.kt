package com.vancoding.pokemon.domain.repository

import com.vancoding.pokemon.domain.model.Pokemon
import com.vancoding.pokemon.domain.model.PokemonDetails
import com.vancoding.pokemon.utils.NetworkResultState
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): Flow<NetworkResultState<List<Pokemon>>>
    suspend fun getPokemonDetails(id: Int): Flow<NetworkResultState<PokemonDetails>>
}