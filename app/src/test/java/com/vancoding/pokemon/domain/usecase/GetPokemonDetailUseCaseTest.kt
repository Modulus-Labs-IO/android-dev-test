package com.vancoding.pokemon.domain.usecase

import com.vancoding.pokemon.domain.model.PokemonDetails
import com.vancoding.pokemon.domain.repository.PokemonRepository
import com.vancoding.pokemon.domain.usecases.GetPokemonDetailUseCase
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
class GetPokemonDetailUseCaseTest {

    @Mock
    private lateinit var repository: PokemonRepository

    private lateinit var getPokemonDetailUseCase: GetPokemonDetailUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getPokemonDetailUseCase = GetPokemonDetailUseCase(repository)
    }

    @Test
    fun `invoke returns flow from repository`() = runTest {
        // Given
        val pokemonId = 1
        val pokemonDetails = PokemonDetails(
            id = 1,
            name = "bulbasaur",
            imageUrl = "url",
            height = 7,
            weight = 69,
            types = listOf("grass", "poison"),
            abilities = listOf("overgrow", "chlorophyll"),
            baseExperience = 64,
            stats = mapOf("hp" to 45, "attack" to 49)
        )

        val expectedFlow = flowOf(NetworkResultState.Success(pokemonDetails))
        `when`(repository.getPokemonDetails(pokemonId)).thenReturn(expectedFlow)

        // When
        val result = getPokemonDetailUseCase(pokemonId)

        // Then
        val firstEmission = result.first()
        assert(firstEmission is NetworkResultState.Success)
        assertEquals(pokemonDetails, (firstEmission as NetworkResultState.Success).data)
    }
}
