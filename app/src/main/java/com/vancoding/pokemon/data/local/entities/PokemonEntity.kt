package com.vancoding.pokemon.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vancoding.pokemon.domain.model.Pokemon

@Entity(tableName = "pokemon_table")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val url: String,
) {
    fun toPokemon(): Pokemon {
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