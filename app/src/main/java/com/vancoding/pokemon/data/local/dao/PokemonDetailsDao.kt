package com.vancoding.pokemon.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vancoding.pokemon.data.local.entities.PokemonDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetails(pokemonDetails: PokemonDetailsEntity)

    @Query("SELECT * FROM pokemon_details WHERE id = :id")
    fun getPokemonDetails(id: Int): Flow<PokemonDetailsEntity?>

    @Query("DELETE FROM pokemon_details")
    suspend fun clearAll()
}