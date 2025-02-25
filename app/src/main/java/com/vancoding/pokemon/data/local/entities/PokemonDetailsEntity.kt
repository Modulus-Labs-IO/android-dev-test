package com.vancoding.pokemon.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vancoding.pokemon.domain.model.PokemonDetails

@Entity(tableName = "pokemon_details")
data class PokemonDetailsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val height: Int,
    val weight: Int,
    val imageUrl: String,
    val types: String,
    val abilities: String,
    val baseExperience: Int,
    val stats: String,
) {
    fun toDomain(): PokemonDetails {
        return PokemonDetails(
            id = id,
            name = name,
            height = height,
            weight = weight,
            imageUrl = imageUrl,
            types = types.split(","),
            abilities = abilities.split(","),
            baseExperience = baseExperience,
            stats = stats.split(",").associate {
                val keyValue = it.split(":")
                keyValue[0] to keyValue[1].toInt()
            }
        )
    }

    companion object {
        fun fromDomain(domain: PokemonDetails): PokemonDetailsEntity {
            return PokemonDetailsEntity(
                id = domain.id,
                name = domain.name,
                height = domain.height,
                weight = domain.weight,
                imageUrl = domain.imageUrl,
                types = domain.types.joinToString(","),
                abilities = domain.abilities.joinToString(","),
                baseExperience = domain.baseExperience,
                stats = domain.stats.entries.joinToString(",") { "${it.key}:${it.value}" }
            )
        }
    }
}