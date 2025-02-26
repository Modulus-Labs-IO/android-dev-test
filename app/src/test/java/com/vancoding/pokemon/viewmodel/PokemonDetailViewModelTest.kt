package com.vancoding.pokemon.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vancoding.pokemon.domain.model.PokemonDetails
import com.vancoding.pokemon.domain.usecases.GetPokemonDetailUseCase
import com.vancoding.pokemon.utils.MainCoroutineRule
import com.vancoding.pokemon.utils.NetworkResultState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PokemonDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var getPokemonDetailUseCase: GetPokemonDetailUseCase

    private lateinit var viewModel: PokemonDetailViewModel

    private val mockPokemonDetails = PokemonDetails(
        id = 1,
        name = "bulbasaur",
        imageUrl = "url",
        height = 7,
        weight = 69,
        types = listOf("grass", "poison"),
        abilities = listOf("overgrow", "chlorophyll"),
        baseExperience = 64,
        stats = mapOf(
            "hp" to 45,
            "attack" to 49,
            "defense" to 49,
            "special-attack" to 65,
            "special-defense" to 65,
            "speed" to 45
        ),
    )

    @Before
    fun setup() = runTest {
        `when`(getPokemonDetailUseCase(anyInt())).thenReturn(
            flowOf(NetworkResultState.Success(mockPokemonDetails))
        )
        viewModel = PokemonDetailViewModel(getPokemonDetailUseCase)
    }

    @Test
    fun `getPokemonDetails calls use case and updates state`() = runTest {
        // Given
        val pokemonId = 1

        // When
        viewModel.getPokemonDetails(pokemonId)
        advanceUntilIdle()

        // Then
        verify(getPokemonDetailUseCase).invoke(pokemonId)

        val currentState = viewModel.pokemonDetails.value
        assert(currentState is NetworkResultState.Success)
        assertEquals(mockPokemonDetails, (currentState as NetworkResultState.Success).data)
    }

    @Test
    fun `getPokemonDetails propagates loading state`() = runTest {
        // Given
        val pokemonId = 2
        `when`(getPokemonDetailUseCase(pokemonId)).thenReturn(
            flowOf(NetworkResultState.Loading())
        )

        // When
        viewModel.getPokemonDetails(pokemonId)
        advanceUntilIdle()

        // Then
        assert(viewModel.pokemonDetails.value is NetworkResultState.Loading)
    }

    @Test
    fun `getPokemonDetails propagates error state`() = runTest {
        // Given
        val pokemonId = 3
        val errorMessage = "Pokemon not found"
        `when`(getPokemonDetailUseCase(pokemonId)).thenReturn(
            flowOf(NetworkResultState.Failure(errorMessage))
        )

        // When
        viewModel.getPokemonDetails(pokemonId)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.pokemonDetails.value
        assert(currentState is NetworkResultState.Failure)
        assertEquals(errorMessage, (currentState as NetworkResultState.Failure).message)
    }
}