package com.vancoding.pokemon.di

import android.content.Context
import androidx.room.Room
import com.vancoding.pokemon.data.local.PokemonDatabase
import com.vancoding.pokemon.utils.Constants.POKEMON_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePokemonDatabase(
        @ApplicationContext context: Context,
    ): PokemonDatabase {
        return Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            POKEMON_DATABASE_NAME,
        ).build()
    }
}