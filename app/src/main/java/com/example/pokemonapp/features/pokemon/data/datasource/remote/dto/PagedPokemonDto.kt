package com.example.pokemonapp.features.pokemon.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

data class PagedPokemonDto(
    val count: Int,
    val next: String,
    val previous: String,
    @SerializedName("results")
    val pokemonResults: List<PokemonResultDto>
)

data class PokemonResultDto(
    val name: String,
    val url: String
)