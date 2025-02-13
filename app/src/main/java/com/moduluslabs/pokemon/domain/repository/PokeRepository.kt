package com.moduluslabs.pokemon.domain.repository

import com.moduluslabs.pokemon.domain.model.PokeResult
import com.moduluslabs.pokemon.domain.model.Pokemon
import com.moduluslabs.pokemon.domain.model.PokemonDetails
import com.moduluslabs.pokemon.utils.ResourceState
import kotlinx.coroutines.flow.Flow

interface PokeRepository {

    suspend fun getPokemon(limit: String, offset: Int): ResourceState<Pokemon>
    suspend fun getPokemonDetails(pokemonName: String): ResourceState<PokemonDetails>
    suspend fun savePokemon(pokeResult: PokeResult)
    fun getSavedPokemon(): Flow<List<PokeResult>>
}