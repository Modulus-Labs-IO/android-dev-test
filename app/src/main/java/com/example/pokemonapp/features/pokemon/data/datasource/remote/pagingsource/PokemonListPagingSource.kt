package com.example.pokemonapp.features.pokemon.data.datasource.remote.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.withTransaction
import com.example.pokemonapp.core.network.safeCall
import com.example.pokemonapp.core.util.map
import com.example.pokemonapp.core.util.onError
import com.example.pokemonapp.core.util.onSuccess
import com.example.pokemonapp.features.pokemon.data.datasource.local.database.PokemonDatabase
import com.example.pokemonapp.features.pokemon.data.datasource.remote.PokemonApiService
import com.example.pokemonapp.features.pokemon.data.mapper.mapToDomainModel
import com.example.pokemonapp.features.pokemon.data.mapper.mapToEntity
import com.example.pokemonapp.features.pokemon.domain.model.PokemonListItem

class PokemonListPagingSource(
    private val api: PokemonApiService,
    private val database: PokemonDatabase
): PagingSource<Int, PokemonListItem>() {
    override fun getRefreshKey(state: PagingState<Int, PokemonListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonListItem> {
        return try {
            val page = params.key ?: 0 // Start from page 0
            val pokemonList = mutableListOf<PokemonListItem>()
            safeCall {
                api.getPokemonList(offset = page * PAGE_SIZE, limit = PAGE_SIZE)
            }.map {
                it.pokemonResults.map { result ->
                    result.mapToDomainModel()
                }
            }.onSuccess { result ->
                pokemonList.addAll(result)
            }.onError { _, message->
                return LoadResult.Error(Exception(message))
            }
            database.withTransaction {
                database.dao.insertPokemons(pokemonList.map { it.mapToEntity() })
            }
            LoadResult.Page(
                data = pokemonList,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (pokemonList.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}