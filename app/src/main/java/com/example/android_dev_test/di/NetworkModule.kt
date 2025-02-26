package com.example.android_dev_test.di

import com.example.android_dev_test.data.network.ApiService
import com.example.android_dev_test.data.network.PokemonRetrofit
import com.example.android_dev_test.data.repository.PokemonRepository
import com.example.android_dev_test.data.repository.PokemonRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providePokemonService(): ApiService {
        return PokemonRetrofit.pokemonApiService
    }

    @Provides
    @Singleton
    fun providePokemonRepository(pokemonService: ApiService): PokemonRepository {
        return PokemonRepositoryImpl(pokemonService)
    }

}