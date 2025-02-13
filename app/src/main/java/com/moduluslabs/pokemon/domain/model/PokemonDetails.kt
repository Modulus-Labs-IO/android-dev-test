package com.moduluslabs.pokemon.domain.model

import com.google.gson.annotations.SerializedName

data class PokemonDetails(
    @SerializedName("id")
    val pokeId: String,
    @SerializedName("base_experience")
    val baseExp: String,
    val weight: String,
    val height: String
)
