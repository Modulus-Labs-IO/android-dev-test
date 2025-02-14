package com.example.pokemonapp.features.pokemon.di

import com.example.pokemonapp.features.pokemon.data.datasource.local.database.PokemonDatabase
import com.example.pokemonapp.features.pokemon.data.datasource.remote.PokemonApiService
import com.example.pokemonapp.features.pokemon.data.repository.PokemonRepositoryImpl
import com.example.pokemonapp.features.pokemon.domain.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun providePokemonRepository(api: PokemonApiService, database: PokemonDatabase): PokemonRepository {
        return PokemonRepositoryImpl(api, database)
    }

}