package com.example.pokemonapp.features.pokemon.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokemonapp.features.pokemon.data.datasource.local.entity.PokemonEntity

@Database(
    entities = [PokemonEntity::class],
    version = 1
)
abstract class PokemonDatabase: RoomDatabase() {

    abstract val dao: PokemonDao

}