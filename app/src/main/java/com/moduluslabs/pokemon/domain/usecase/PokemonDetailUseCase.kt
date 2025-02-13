package com.moduluslabs.pokemon.domain.usecase

import com.moduluslabs.pokemon.domain.model.PokemonDetails
import com.moduluslabs.pokemon.domain.repository.PokeRepository
import com.moduluslabs.pokemon.utils.ResourceState
import javax.inject.Inject

class PokemonDetailUseCase @Inject constructor(
    private val pokeRepository: PokeRepository
) {
    suspend fun invoke(pokemonName: String): ResourceState<PokemonDetails>
        = pokeRepository.getPokemonDetails(pokemonName)
}