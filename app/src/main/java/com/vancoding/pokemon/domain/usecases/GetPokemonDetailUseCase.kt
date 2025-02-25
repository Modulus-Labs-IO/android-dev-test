package com.vancoding.pokemon.domain.usecases

import com.vancoding.pokemon.domain.model.PokemonDetails
import com.vancoding.pokemon.domain.repository.PokemonRepository
import com.vancoding.pokemon.utils.NetworkResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonDetailUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository,
) {
    suspend operator fun invoke(id: Int): Flow<NetworkResultState<PokemonDetails>> {
        return pokemonRepository.getPokemonDetails(id)
    }
}