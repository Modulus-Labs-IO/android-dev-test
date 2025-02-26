package com.vancoding.pokemon.data.repository

import com.vancoding.pokemon.data.local.PokemonDatabase
import com.vancoding.pokemon.data.local.entities.PokemonDetailsEntity
import com.vancoding.pokemon.data.local.entities.PokemonEntity
import com.vancoding.pokemon.data.mappers.toDomain
import com.vancoding.pokemon.data.remote.api.PokeApiService
import com.vancoding.pokemon.domain.model.PokemonDetails
import com.vancoding.pokemon.domain.repository.PokemonRepository
import com.vancoding.pokemon.domain.model.Pokemon
import com.vancoding.pokemon.utils.Constants.ERROR_RESPONSE_BODY_NULL
import com.vancoding.pokemon.utils.Constants.ERROR_UNKNOWN
import com.vancoding.pokemon.utils.Constants.ONE_DAY_IN_MS
import com.vancoding.pokemon.utils.Constants.POKEMON_LIST_LIMIT
import com.vancoding.pokemon.utils.NetworkResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokeApiService,
    private val database: PokemonDatabase,
) : PokemonRepository {

    override suspend fun getPokemonList(limit: Int, offset: Int): Flow<NetworkResultState<List<Pokemon>>> = flow {
        emit(NetworkResultState.Loading())

        val localData = fetchPokemonListFromDatabase(limit, offset)
        if (localData.isNotEmpty()) {
            emit(NetworkResultState.Success(localData))
        }

        try {
            val remoteData = fetchPokemonListFromApi(limit, offset)
            database.pokemonDao().insertPokemonList(remoteData.map { PokemonEntity.fromDomain(it) })

            if (remoteData != localData) {
                emit(NetworkResultState.Success(remoteData))
            }
        } catch (e: Exception) {
            if (localData.isEmpty()) {
                emit(NetworkResultState.Failure(e.message ?: ERROR_UNKNOWN))
            }
        }
    }.distinctUntilChanged()

    override suspend fun getPokemonDetails(id: Int): Flow<NetworkResultState<PokemonDetails>> = flow {
        emit(NetworkResultState.Loading())

        val localData = database.pokemonDetailsDao().getPokemonDetails(id).firstOrNull()

        // Checking the stored data is older than 24 hours
        val needsUpdate = localData == null || (System.currentTimeMillis() - localData.lastUpdated > ONE_DAY_IN_MS)

        if (localData != null) {
            emit(NetworkResultState.Success(localData.toDomain()))
        }

        if (needsUpdate) { // Fetch only if the data is outdated
            try {
                val response = api.getPokemonDetails(id)
                if (response.isSuccessful) {
                    response.body()?.let { apiResponse ->
                        val pokemonDetails = apiResponse.toDomain()
                        database.pokemonDetailsDao().insertPokemonDetails(PokemonDetailsEntity.fromDomain(pokemonDetails))
                        emit(NetworkResultState.Success(pokemonDetails))
                    } ?: emit(NetworkResultState.Failure(ERROR_RESPONSE_BODY_NULL))
                } else if (localData == null) {
                    emit(NetworkResultState.Failure(response.message()))
                }
            } catch (e: Exception) {
                if (localData == null) {
                    emit(NetworkResultState.Failure(e.message ?: ERROR_UNKNOWN))
                }
            }
        }
    }.catch { e ->
        emit(NetworkResultState.Failure(e.message ?: ERROR_UNKNOWN))
    }

    override suspend fun searchPokemon(query: String): Flow<NetworkResultState<List<Pokemon>>> = flow {
        emit(NetworkResultState.Loading())

        val localData = database.pokemonDao().searchPokemon(query)
            .map { entities -> entities.map { it.toPokemon() } }
            .firstOrNull() ?: emptyList()

        if (localData.isNotEmpty()) {
            emit(NetworkResultState.Success(localData))
            return@flow
        }

        try {
            val remoteData = fetchPokemonListFromApi(POKEMON_LIST_LIMIT, 0)
            database.pokemonDao().insertPokemonList(remoteData.map { PokemonEntity.fromDomain(it) })

            val updatedResults = database.pokemonDao().searchPokemon(query)
                .map { entities -> entities.map { it.toPokemon() } }
                .firstOrNull() ?: emptyList()

            emit(NetworkResultState.Success(updatedResults))
        } catch (e: Exception) {
            emit(NetworkResultState.Failure(e.message ?: ERROR_UNKNOWN))
        }
    }.catch { e ->
        emit(NetworkResultState.Failure(e.message ?: ERROR_UNKNOWN))
    }

    private suspend fun fetchPokemonListFromApi(limit: Int, offset: Int): List<Pokemon> {
        val response = api.getPokemonList(limit, offset)
        return if (response.isSuccessful) {
            response.body()?.toDomain() ?: throw Exception(ERROR_RESPONSE_BODY_NULL)
        } else {
            throw Exception(response.message())
        }
    }

    private suspend fun fetchPokemonListFromDatabase(limit: Int, offset: Int): List<Pokemon> {
        return database.pokemonDao().getPokemonList(limit, offset)
            .map { entities -> entities.map { it.toPokemon() } }
            .firstOrNull() ?: emptyList()
    }
}