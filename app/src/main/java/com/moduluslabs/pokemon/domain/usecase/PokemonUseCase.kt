package com.moduluslabs.pokemon.domain.usecase

import com.moduluslabs.pokemon.domain.model.Pokemon
import com.moduluslabs.pokemon.domain.repository.PokeRepository
import com.moduluslabs.pokemon.utils.ResourceState
import javax.inject.Inject

class PokemonUseCase @Inject constructor(
    private val pokeRepository: PokeRepository
) {
    suspend fun invoke(limit: String, offset: Int): ResourceState<Pokemon>
            = pokeRepository.getPokemon(limit, offset)
}