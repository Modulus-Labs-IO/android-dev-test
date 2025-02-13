package com.moduluslabs.pokemon.data.repository.datasource

import com.moduluslabs.pokemon.domain.model.Pokemon
import com.moduluslabs.pokemon.domain.model.PokemonDetails
import retrofit2.Response


interface PokeRemoteSource {

    suspend fun getPokemon(limit: String, offset: Int): Response<Pokemon>
    suspend fun getPokemonDetails(pokemonName: String): Response<PokemonDetails>

}