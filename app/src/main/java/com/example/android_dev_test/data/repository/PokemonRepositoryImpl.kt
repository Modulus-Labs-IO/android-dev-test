package com.example.android_dev_test.data.repository

import com.example.android_dev_test.data.model.PokemonDetailsResponse
import com.example.android_dev_test.data.model.PokemonResponse
import com.example.android_dev_test.data.network.ApiService
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    PokemonRepository {
    override suspend fun getPokemonList(limit: Int, offset: Int): PokemonResponse {
        return apiService.getPokemonList(limit,offset)
    }

    override suspend fun getPokemonDetails(name: String): PokemonDetailsResponse {
        return apiService.getPokemonDetails(name)
    }
}