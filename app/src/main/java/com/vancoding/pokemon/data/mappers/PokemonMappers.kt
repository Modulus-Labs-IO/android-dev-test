package com.vancoding.pokemon.data.mappers

import com.vancoding.pokemon.data.remote.response.PokemonDetailsResponse
import com.vancoding.pokemon.data.remote.response.PokemonListResponse
import com.vancoding.pokemon.domain.model.Pokemon
import com.vancoding.pokemon.domain.model.PokemonDetails

fun PokemonListResponse.toDomain(): List<Pokemon> {
    return this.results.map {
        Pokemon(
            id = extractIdFromUrl(it.url),
            name = it.name,
            url = it.url,
        )
    }
}

fun PokemonDetailsResponse.toDomain(): PokemonDetails {
    return PokemonDetails(
        id = this.id,
        name = this.name,
        height = this.height,
        weight = this.weight,
        imageUrl = this.sprites.frontDefault.orEmpty(),
        types = this.types.map { it.type.name },
        abilities = this.abilities.map { it.ability.name },
        baseExperience = this.baseExperience,
        stats = this.stats.associate { it.stat.name to it.baseStat },
    )
}

private fun extractIdFromUrl(url: String): Int {
    return url.substringAfter("pokemon/").substringBefore("/").toInt()
}