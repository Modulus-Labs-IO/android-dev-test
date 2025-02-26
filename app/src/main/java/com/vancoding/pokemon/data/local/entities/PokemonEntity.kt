package com.vancoding.pokemon.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vancoding.pokemon.domain.model.Pokemon
import com.vancoding.pokemon.utils.Constants.POKEMON_TABLE_NAME

@Entity(tableName = POKEMON_TABLE_NAME)
data class PokemonEntity(
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    val url: String,
) {
    fun toDomainModel(): Pokemon {
        return Pokemon(
            id = id,
            name = name,
            url = url,
        )
    }

    companion object {
        fun fromDomain(domain: Pokemon): PokemonEntity {
            return PokemonEntity(
                id = domain.id,
                name = domain.name,
                url = domain.url,
            )
        }
    }
}