package com.example.pokemonapp.features.pokemon.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pokemonapp.core.network.safeCall
import com.example.pokemonapp.core.util.DataError
import com.example.pokemonapp.core.util.Result
import com.example.pokemonapp.core.util.map
import com.example.pokemonapp.core.util.onError
import com.example.pokemonapp.core.util.onSuccess
import com.example.pokemonapp.features.pokemon.data.datasource.local.database.PokemonDatabase
import com.example.pokemonapp.features.pokemon.data.datasource.remote.PokemonApiService
import com.example.pokemonapp.features.pokemon.data.datasource.remote.dto.PokemonDetailDto
import com.example.pokemonapp.features.pokemon.data.datasource.remote.pagingsource.PokemonListPagingSource
import com.example.pokemonapp.features.pokemon.data.mapper.mapToDomainModel
import com.example.pokemonapp.features.pokemon.domain.model.PokemonListItem
import com.example.pokemonapp.features.pokemon.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokemonApiService,
    private val database: PokemonDatabase
): PokemonRepository {
    override fun getPokemonList(): Flow<PagingData<PokemonListItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PokemonListPagingSource(api, database) }
        ).flow
    }

    override suspend fun searchPokemon(name: String): Flow<List<PokemonListItem>> = flow {
        safeCall {
            api.getPokemonList(
                offset = 0,
                limit = 1000
            )
        }.onSuccess {
            emit(it.pokemonResults.map { result ->
                result.mapToDomainModel()
            }.filter { pokemon ->
                pokemon.name.contains(name, ignoreCase = true)
            })
        }.onError { error, message ->
            emit(emptyList())
        }
    }

}