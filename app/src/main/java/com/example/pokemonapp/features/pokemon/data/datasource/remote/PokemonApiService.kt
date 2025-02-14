package com.example.pokemonapp.features.pokemon.data.datasource.remote

import com.example.pokemonapp.features.pokemon.data.datasource.remote.dto.PagedPokemonDto
import com.example.pokemonapp.features.pokemon.data.datasource.remote.dto.PokemonDetailDto
import com.example.pokemonapp.features.pokemon.domain.model.PokemonListItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): Response<PagedPokemonDto>

    @GET("pokemon/{name}")
    suspend fun searchPokemon(
        @Path("name") name: String
    ): Response<PokemonDetailDto>

}