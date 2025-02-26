package com.vancoding.pokemon.domain.usecase

import com.vancoding.pokemon.domain.model.Pokemon
import com.vancoding.pokemon.domain.repository.PokemonRepository
import com.vancoding.pokemon.domain.usecases.GetPokemonListUseCase
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
class GetPokemonListUseCaseTest {

    @Mock
    private lateinit var repository: PokemonRepository

    private lateinit var getPokemonListUseCase: GetPokemonListUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getPokemonListUseCase = GetPokemonListUseCase(repository)
    }

    @Test
    fun `invoke returns flow from repository`() = runTest {
        // Given
        val limit = 20
        val offset = 0
        val pokemonList = listOf(
            Pokemon(1, "bulbasaur", "url"),
            Pokemon(2, "ivysaur", "url")
        )

        val expectedFlow = flowOf(NetworkResultState.Success(pokemonList))
        `when`(repository.getPokemonList(limit, offset)).thenReturn(expectedFlow)

        // When
        val result = getPokemonListUseCase(limit, offset)

        // Then
        val firstEmission = result.first()
        assert(firstEmission is NetworkResultState.Success)
        assertEquals(pokemonList, (firstEmission as NetworkResultState.Success).data)
    }
}