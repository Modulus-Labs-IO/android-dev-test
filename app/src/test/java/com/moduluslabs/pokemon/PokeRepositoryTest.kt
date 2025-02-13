package com.moduluslabs.pokemon

import com.moduluslabs.pokemon.data.repository.PokeRepositoryImpl
import com.moduluslabs.pokemon.data.repository.datasource.PokeLocalSource
import com.moduluslabs.pokemon.data.repository.datasource.PokeRemoteSource
import com.moduluslabs.pokemon.domain.model.PokeResult
import com.moduluslabs.pokemon.domain.repository.PokeRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class PokeRepositoryTest {

    // Mock dependencies
    private val pokeRemoteSource: PokeRemoteSource = mockk()
    private val pokeLocalSource: PokeLocalSource = mockk()

    // Class under test
    private lateinit var pokeRepository: PokeRepository

    @Before
    fun setUp() {
        pokeRepository = PokeRepositoryImpl(pokeRemoteSource, pokeLocalSource)
    }

    @Test
    fun `verify getPokemon is called`() = runTest {
        // Arrange
        val limit = "10"
        val offset = 0
        coEvery { pokeRemoteSource.getPokemon(limit, offset) } returns Response.success(mockk())

        // Act
        pokeRepository.getPokemon(limit, offset)

        // Assert
        coVerify { pokeRemoteSource.getPokemon(limit, offset) }
    }

    @Test
    fun `verify getPokemonDetails is called`() = runTest {
        // Arrange
        val pokemonName = "pikachu"
        coEvery { pokeRemoteSource.getPokemonDetails(pokemonName) } returns Response.success(mockk())

        // Act
        pokeRepository.getPokemonDetails(pokemonName)

        // Assert
        coVerify { pokeRemoteSource.getPokemonDetails(pokemonName) }
    }

    @Test
    fun `verify savePokemon is called`() = runTest {
        // Arrange
        val pokeResult = PokeResult(name = "Pikachu", url = "")

        coEvery { pokeLocalSource.savePokemon(pokeResult) } just Runs

        // Act
        pokeRepository.savePokemon(pokeResult)

        // Assert
        coVerify { pokeLocalSource.savePokemon(pokeResult) }
    }

    @Test
    fun `verify getSavedPokemon is called`() {
        // Arrange
        every { pokeLocalSource.getSavedPokemon() } returns mockk()

        // Act
        pokeRepository.getSavedPokemon()

        // Assert
        coVerify { pokeLocalSource.getSavedPokemon() }
    }
}
