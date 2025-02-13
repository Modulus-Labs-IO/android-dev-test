package com.moduluslabs.pokemon.data.repository.datasource

import com.moduluslabs.pokemon.domain.model.PokeResult
import kotlinx.coroutines.flow.Flow

interface PokeLocalSource {

    suspend fun savePokemon(pokeResult: PokeResult)
    fun getSavedPokemon(): Flow<List<PokeResult>>
}