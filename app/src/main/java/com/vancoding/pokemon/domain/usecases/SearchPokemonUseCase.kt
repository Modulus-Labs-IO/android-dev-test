package com.vancoding.pokemon.domain.usecases

import com.vancoding.pokemon.domain.model.Pokemon
import com.vancoding.pokemon.domain.repository.PokemonRepository
import com.vancoding.pokemon.utils.NetworkResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPokemonUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository,
) {
    suspend operator fun invoke(query: String): Flow<NetworkResultState<List<Pokemon>>> {
        return pokemonRepository.searchPokemon(query)
    }
}