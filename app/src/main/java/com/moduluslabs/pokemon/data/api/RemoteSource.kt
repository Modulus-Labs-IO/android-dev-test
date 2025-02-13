package com.moduluslabs.pokemon.data.api

import com.moduluslabs.pokemon.domain.model.Pokemon
import com.moduluslabs.pokemon.domain.model.PokemonDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteSource {

    @GET("pokemon")
    suspend fun getPokemon(
        @Query("limit") limit: String,
        @Query("offset") offset: Int
    ): Response<Pokemon>

    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(
        @Path("name") pokemonName: String
    ): Response<PokemonDetails>
}