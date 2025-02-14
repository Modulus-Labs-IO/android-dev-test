package com.example.pokemonapp.features.pokemon.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonDetailDto(
    val name: String,
    val weight: Long,
    val height: Long,
    val sprites: Sprites,
    val stats: List<Stats>
)

data class Sprites(
    @SerializedName("front_default")
    val frontDefault: String
)

data class Stat(
    val name: String,
    val url: String
)

data class Stats(
    @SerializedName("base_stat")
    val baseState: Long,
    val effort: Long,
    val stat: Stat
)
