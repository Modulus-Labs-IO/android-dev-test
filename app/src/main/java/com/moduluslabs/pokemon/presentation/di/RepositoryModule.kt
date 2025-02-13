package com.moduluslabs.pokemon.presentation.di

import com.moduluslabs.pokemon.data.repository.PokeRepositoryImpl
import com.moduluslabs.pokemon.domain.repository.PokeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindsPokeRepository(pokeRepositoryImpl: PokeRepositoryImpl): PokeRepository

}