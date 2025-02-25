package com.vancoding.pokemon.domain.model

data class PokemonDetails(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val imageUrl: String,
    val types: List<String>,
    val abilities: List<String>,
    val baseExperience: Int,
    val stats: Map<String, Int>,
)