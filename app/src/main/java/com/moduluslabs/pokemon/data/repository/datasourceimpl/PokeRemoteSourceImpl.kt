package com.moduluslabs.pokemon.data.repository.datasourceimpl

import com.moduluslabs.pokemon.data.api.RemoteSource
import com.moduluslabs.pokemon.data.repository.datasource.PokeRemoteSource
import com.moduluslabs.pokemon.domain.model.Pokemon
import com.moduluslabs.pokemon.domain.model.PokemonDetails
import retrofit2.Response
import javax.inject.Inject

class PokeRemoteSourceImpl @Inject constructor(
    private val remoteSource: RemoteSource
): PokeRemoteSource {

    override suspend fun getPokemon(limit: String, offset: Int): Response<Pokemon>
        = remoteSource.getPokemon(limit, offset)

    override suspend fun getPokemonDetails(pokemonName: String): Response<PokemonDetails>
        = remoteSource.getPokemonDetails(pokemonName)
}