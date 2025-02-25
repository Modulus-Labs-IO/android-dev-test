package com.vancoding.pokemon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vancoding.pokemon.data.local.dao.PokemonDao
import com.vancoding.pokemon.data.local.dao.PokemonDetailsDao
import com.vancoding.pokemon.data.local.entities.PokemonDetailsEntity
import com.vancoding.pokemon.data.local.entities.PokemonEntity

@Database(
    entities = [PokemonEntity::class, PokemonDetailsEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun pokemonDetailsDao(): PokemonDetailsDao
}