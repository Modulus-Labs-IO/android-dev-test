package com.moduluslabs.pokemon.domain.usecase

import com.moduluslabs.pokemon.domain.model.PokeResult
import com.moduluslabs.pokemon.domain.repository.PokeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedPokemonUseCase @Inject constructor(
    private val pokeRepository: PokeRepository
) {
    fun invoke(): Flow<List<PokeResult>>
        = pokeRepository.getSavedPokemon()
}