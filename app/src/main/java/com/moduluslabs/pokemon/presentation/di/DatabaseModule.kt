package com.moduluslabs.pokemon.presentation.di

import android.app.Application
import androidx.room.Room
import com.moduluslabs.pokemon.data.db.PokeDao
import com.moduluslabs.pokemon.data.db.PokeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesMovieDatabase(app: Application): PokeDatabase{
        return Room.databaseBuilder(app, PokeDatabase::class.java,"poke_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesMovieDao(movieDatabase: PokeDatabase): PokeDao{
        return movieDatabase.getPokeDao()
    }

}