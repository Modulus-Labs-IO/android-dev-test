package com.vancoding.pokemon.di

import com.vancoding.pokemon.data.local.PokemonDatabase
import com.vancoding.pokemon.data.remote.api.PokeApiService
import com.vancoding.pokemon.data.repository.PokemonRepositoryImpl
import com.vancoding.pokemon.domain.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePokemonRepository(
        api: PokeApiService,
        database: PokemonDatabase,
    ): PokemonRepository {
        return PokemonRepositoryImpl(api, database)
    }
}