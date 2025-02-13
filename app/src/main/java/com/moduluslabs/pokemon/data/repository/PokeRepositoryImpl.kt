package com.moduluslabs.pokemon.data.repository

import com.moduluslabs.pokemon.data.repository.datasource.PokeLocalSource
import com.moduluslabs.pokemon.data.repository.datasource.PokeRemoteSource
import com.moduluslabs.pokemon.domain.model.PokeResult
import com.moduluslabs.pokemon.domain.model.Pokemon
import com.moduluslabs.pokemon.domain.model.PokemonDetails
import com.moduluslabs.pokemon.domain.repository.PokeRepository
import com.moduluslabs.pokemon.utils.ResourceState
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class PokeRepositoryImpl @Inject constructor(
    private val pokeRemoteSource: PokeRemoteSource,
    private val pokeLocalSource: PokeLocalSource
): PokeRepository {

    private fun <T> responseToResource(response: Response<T>): ResourceState<T> {
        return if (response.isSuccessful) {
            response.body()?.let {
                ResourceState.Success(it)
            } ?: ResourceState.Error("Response body is null")
        } else {
            ResourceState.Error("Error: ${response.code()}")
        }
    }

    override suspend fun getPokemon(limit: String, offset: Int): ResourceState<Pokemon>
        = responseToResource(pokeRemoteSource.getPokemon(limit, offset))

    override suspend fun getPokemonDetails(pokemonName: String): ResourceState<PokemonDetails>
        = responseToResource(pokeRemoteSource.getPokemonDetails(pokemonName))

    override suspend fun savePokemon(pokeResult: PokeResult)
        = pokeLocalSource.savePokemon(pokeResult)

    override fun getSavedPokemon(): Flow<List<PokeResult>>
        = pokeLocalSource.getSavedPokemon()

}