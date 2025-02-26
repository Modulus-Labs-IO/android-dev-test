package com.vancoding.pokemon.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vancoding.pokemon.domain.model.PokemonDetails
import com.vancoding.pokemon.utils.Constants.POKEMON_DETAIL_TABLE_NAME

@Entity(tableName = POKEMON_DETAIL_TABLE_NAME)
data class PokemonDetailsEntity(
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    val height: Int,
    val weight: Int,
    val imageUrl: String,
    val typesAsString: String,
    val abilitiesAsString: String,
    val baseExperience: Int,
    val statsAsString: String,
    val lastFetchedTimestamp: Long = System.currentTimeMillis(),
) {
    fun toPokemonDetailsDomain(): PokemonDetails {
        return PokemonDetails(
            id = id,
            name = name,
            height = height,
            weight = weight,
            imageUrl = imageUrl,
            types = typesAsString.split(","),
            abilities = abilitiesAsString.split(","),
            baseExperience = baseExperience,
            stats = statsAsString.split(",").associate {
                val keyValue = it.split(":")
                keyValue[0] to keyValue[1].toInt()
            },
        )
    }

    companion object {
        fun toEntity(pokemonDetails: PokemonDetails): PokemonDetailsEntity {
            return PokemonDetailsEntity(
                id = pokemonDetails.id,
                name = pokemonDetails.name,
                height = pokemonDetails.height,
                weight = pokemonDetails.weight,
                imageUrl = pokemonDetails.imageUrl,
                typesAsString = pokemonDetails.types.joinToString(","),
                abilitiesAsString = pokemonDetails.abilities.joinToString(","),
                baseExperience = pokemonDetails.baseExperience,
                statsAsString = pokemonDetails.stats.entries.joinToString(",") { "${it.key}:${it.value}" },
                lastFetchedTimestamp = System.currentTimeMillis(),
            )
        }
    }
}