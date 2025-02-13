package com.moduluslabs.pokemon.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


data class Pokemon(
    val count: Int,
    val results: List<PokeResult>
)

@Entity(tableName = "pokemon")
data class PokeResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val url: String
): Serializable