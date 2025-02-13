package com.moduluslabs.pokemon.presentation.di

import com.moduluslabs.pokemon.data.repository.datasource.PokeLocalSource
import com.moduluslabs.pokemon.data.repository.datasource.PokeRemoteSource
import com.moduluslabs.pokemon.data.repository.datasourceimpl.PokeLocalSourceImpl
import com.moduluslabs.pokemon.data.repository.datasourceimpl.PokeRemoteSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Singleton
    @Binds
    abstract fun bindPokeRemoteSource(pokeRemoteSourceImpl: PokeRemoteSourceImpl): PokeRemoteSource

    @Singleton
    @Binds
    abstract fun bindPokeLocalSource(pokeLocalSourceImpl: PokeLocalSourceImpl): PokeLocalSource
}