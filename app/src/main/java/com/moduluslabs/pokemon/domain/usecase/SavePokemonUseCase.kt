package com.moduluslabs.pokemon.domain.usecase

import com.moduluslabs.pokemon.domain.model.PokeResult
import com.moduluslabs.pokemon.domain.repository.PokeRepository
import javax.inject.Inject

class SavePokemonUseCase @Inject constructor(
    private val pokeRepository: PokeRepository
) {
    suspend fun invoke(pokeResult: PokeResult)
        = pokeRepository.savePokemon(pokeResult)
}