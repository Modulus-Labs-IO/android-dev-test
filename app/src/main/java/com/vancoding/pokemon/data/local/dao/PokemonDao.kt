package com.vancoding.pokemon.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vancoding.pokemon.data.local.entities.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(pokemonList: List<PokemonEntity>)

    @Query("SELECT * FROM pokemon_table ORDER BY id ASC LIMIT :limit OFFSET :offset")
    fun getPokemonList(limit: Int, offset: Int): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon_table WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%'")
    fun searchPokemon(query: String): Flow<List<PokemonEntity>>

    @Query("DELETE FROM pokemon_table")
    suspend fun clearAll()
}