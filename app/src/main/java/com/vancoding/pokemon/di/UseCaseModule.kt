package com.vancoding.pokemon.di

import com.vancoding.pokemon.domain.repository.PokemonRepository
import com.vancoding.pokemon.domain.usecases.GetPokemonDetailUseCase
import com.vancoding.pokemon.domain.usecases.GetPokemonListUseCase
import com.vancoding.pokemon.domain.usecases.SearchPokemonUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetPokemonListUseCase(
        pokemonRepository: PokemonRepository,
    ): GetPokemonListUseCase {
        return GetPokemonListUseCase(pokemonRepository)
    }

    @Provides
    @Singleton
    fun provideGetPokemonDetailUseCase(
        pokemonRepository: PokemonRepository,
    ): GetPokemonDetailUseCase {
        return GetPokemonDetailUseCase(pokemonRepository)
    }

    @Provides
    @Singleton
    fun provideSearchPokemonUseCase(
        pokemonRepository: PokemonRepository,
    ): SearchPokemonUseCase {
        return SearchPokemonUseCase(pokemonRepository)
    }
}