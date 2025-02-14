package com.example.pokemonapp.features.pokemon.data.datasource.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.pokemonapp.features.pokemon.data.datasource.local.entity.PokemonEntity

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemons(pokemon: List<PokemonEntity>)

    @Query("DELETE FROM pokemonentity")
    suspend fun deleteAllPokemons()

    @Query("SELECT * FROM pokemonentity")
    suspend fun getAllPokemons(): List<PokemonEntity>

    @Query("SELECT * FROM pokemonentity ORDER BY id ASC")
    fun getPagedPokemon(): PagingSource<Int, PokemonEntity>

}