package com.example.pokemonapp.features.pokemon.data.mapper

import com.example.pokemonapp.features.pokemon.data.datasource.local.entity.PokemonEntity
import com.example.pokemonapp.features.pokemon.data.datasource.remote.dto.PokemonResultDto
import com.example.pokemonapp.features.pokemon.domain.model.PokemonListItem

fun PokemonResultDto.mapToDomainModel(): PokemonListItem {
    return PokemonListItem(
        name = name,
        url = url
    )
}

fun PokemonListItem.mapToEntity(): PokemonEntity {
    return PokemonEntity(
        name = name,
        url = url
    )
}

fun PokemonEntity.mapToDomainModel(): PokemonListItem {
    return PokemonListItem(
        name = name,
        url = url
    )
}