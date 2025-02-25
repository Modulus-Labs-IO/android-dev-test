package com.vancoding.pokemon.domain.usecases

import com.vancoding.pokemon.domain.model.Pokemon
import com.vancoding.pokemon.utils.NetworkResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchPokemonUseCase @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
) {
    suspend operator fun invoke(query: String): Flow<NetworkResultState<List<Pokemon>>> = flow {
        getPokemonListUseCase(100, 0).collect { result ->
            when (result) {
                is NetworkResultState.Success -> {
                    val filteredList = result.data.filter { pokemon ->
                        pokemon.name.contains(query, ignoreCase = true)
                    }
                    emit(NetworkResultState.Success(filteredList))
                }
                is NetworkResultState.Failure -> emit(result)
                is NetworkResultState.Loading -> emit(NetworkResultState.Loading())
            }
        }
    }
}