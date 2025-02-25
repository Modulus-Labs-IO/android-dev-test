package com.vancoding.pokemon.data.repository

import com.vancoding.pokemon.data.local.PokemonDatabase
import com.vancoding.pokemon.data.local.entities.PokemonDetailsEntity
import com.vancoding.pokemon.data.local.entities.PokemonEntity
import com.vancoding.pokemon.data.remote.api.PokeApiService
import com.vancoding.pokemon.data.remote.response.PokemonDetailsResponse
import com.vancoding.pokemon.data.remote.response.PokemonListResponse
import com.vancoding.pokemon.domain.model.PokemonDetails
import com.vancoding.pokemon.domain.repository.PokemonRepository
import com.vancoding.pokemon.domain.model.Pokemon
import com.vancoding.pokemon.utils.NetworkResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokeApiService,
    private val database: PokemonDatabase,
): PokemonRepository {

    override suspend fun getPokemonList(limit: Int, offset: Int): Flow<NetworkResultState<List<Pokemon>>> = flow {
        database.pokemonDao().getPokemonList(limit, offset)
            .map { entities -> entities.map { it.toPokemon() } }
            .collect { pokemonList ->
                if (pokemonList.isNotEmpty()) {
                    emit(NetworkResultState.Success(pokemonList))
                }
            }

        try {
            val response = api.getPokemonList(limit, offset)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val pokemonList = apiResponse.mapToDomain()
                    database.pokemonDao()
                        .insertPokemonList(pokemonList.map { PokemonEntity.fromDomain(it) })
                    emit(NetworkResultState.Success(pokemonList))
                } ?: emit(NetworkResultState.Failure("Response body is null"))
            } else {
                emit(NetworkResultState.Failure(response.message()))
            }
        } catch (e: Exception) {
            emit(NetworkResultState.Failure(e.message ?: "Unknown error occurred"))
        }
    }.onStart { emit(NetworkResultState.Loading()) }
        .catch { emit(NetworkResultState.Failure(it.message ?: "Unknown error occurred")) }

    override suspend fun getPokemonDetails(id: Int): Flow<NetworkResultState<PokemonDetails>> = flow {
        database.pokemonDetailsDao().getPokemonDetails(id)
            .collect { pokemonDetails ->
                pokemonDetails?.let {
                    emit(NetworkResultState.Success(it.toDomain()))
                }
            }

        try {
            val response = api.getPokemonDetails(id)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    val pokemonDetails = apiResponse.mapToDomain()
                    database.pokemonDetailsDao().insertPokemonDetails(PokemonDetailsEntity.fromDomain(pokemonDetails))
                    emit(NetworkResultState.Success(pokemonDetails))
                } ?: emit(NetworkResultState.Failure("Response body is null"))
            } else {
                emit(NetworkResultState.Failure(response.message()))
            }
        } catch (e: Exception) {
            emit(NetworkResultState.Failure(e.message ?: "Unknown error occurred"))
        }
    }.onStart { emit(NetworkResultState.Loading()) }
        .catch { emit(NetworkResultState.Failure(it.message ?: "Unknown error occurred")) }

    override suspend fun searchPokemon(query: String): Flow<NetworkResultState<List<Pokemon>>> = flow {
        emit(NetworkResultState.Loading())

        database.pokemonDao().searchPokemon(query)
            .map { entities -> entities.map { it.toPokemon() } }
            .collect { localData ->
                emit(NetworkResultState.Success(localData))
            }
    }.catch { emit(NetworkResultState.Failure(it.message ?: "Unknown error occurred")) }

    private fun PokemonListResponse.mapToDomain(): List<Pokemon> {
        return this.results.map {
            Pokemon(
                id = it.url.substringAfter("pokemon/").substringBefore("/").toInt(),
                name = it.name,
                url = it.url,
            )
        }
    }

    private fun PokemonDetailsResponse.mapToDomain(): PokemonDetails {
        return PokemonDetails(
            id = this.id,
            name = this.name,
            height = this.height,
            weight = this.weight,
            imageUrl = this.sprites.front_default ?: "",
            types = this.types.map { it.type.name },
            abilities = this.abilities.map { it.ability.name },
            baseExperience = this.base_experience,
            stats = this.stats.associate { it.stat.name to it.base_stat },
        )
    }
}