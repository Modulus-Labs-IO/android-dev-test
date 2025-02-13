package com.moduluslabs.pokemon.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moduluslabs.pokemon.domain.model.PokeResult
import kotlinx.coroutines.flow.Flow

@Dao
interface PokeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePokemon(pokeResult: PokeResult)

    @Query("SELECT * FROM pokemon")
    fun getSavedPokemon(): Flow<List<PokeResult>>
}