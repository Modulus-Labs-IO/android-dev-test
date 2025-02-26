package com.vancoding.pokemon.domain.usecase

import com.vancoding.pokemon.domain.model.Pokemon
import com.vancoding.pokemon.domain.repository.PokemonRepository
import com.vancoding.pokemon.domain.usecases.SearchPokemonUseCase
import com.vancoding.pokemon.utils.NetworkResultState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class SearchPokemonUseCaseTest {

    @Mock
    private lateinit var repository: PokemonRepository

    private lateinit var searchPokemonUseCase: SearchPokemonUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        searchPokemonUseCase = SearchPokemonUseCase(repository)
    }

    @Test
    fun `invoke returns flow from repository`() = runTest {
        // Given
        val query = "bulba"
        val pokemonList = listOf(
            Pokemon(1, "bulbasaur", "url")
        )

        val expectedFlow = flowOf(NetworkResultState.Success(pokemonList))
        `when`(repository.searchPokemon(query)).thenReturn(expectedFlow)

        // When
        val result = searchPokemonUseCase(query)

        // Then
        val firstEmission = result.first()
        assert(firstEmission is NetworkResultState.Success)
        assertEquals(pokemonList, (firstEmission as NetworkResultState.Success).data)
    }
}