package com.moduluslabs.pokemon.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moduluslabs.pokemon.domain.model.PokeResult

@Database(
    entities = [PokeResult::class],
    version = 1,
    exportSchema = false
)
abstract class PokeDatabase: RoomDatabase() {
    abstract fun getPokeDao(): PokeDao
}