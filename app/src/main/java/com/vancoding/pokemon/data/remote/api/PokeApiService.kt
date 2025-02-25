package com.vancoding.pokemon.data.remote.api

import com.vancoding.pokemon.data.remote.config.ApiConfig
import com.vancoding.pokemon.data.remote.response.PokemonDetailsResponse
import com.vancoding.pokemon.data.remote.response.PokemonListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET(ApiConfig.EndPoints.POKEMON_LIST)
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): Response<PokemonListResponse>

    @GET(ApiConfig.EndPoints.POKEMON_DETAILS)
    suspend fun getPokemonDetails(
        @Path("id") id: Int,
    ): Response<PokemonDetailsResponse>
}