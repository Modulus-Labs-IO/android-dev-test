package com.moduluslabs.pokemon

import com.moduluslabs.pokemon.data.api.RemoteSource
import com.moduluslabs.pokemon.data.repository.datasource.PokeRemoteSource
import com.moduluslabs.pokemon.data.repository.datasourceimpl.PokeRemoteSourceImpl
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PokeRemoteSourceTest {

    private val remoteSource: RemoteSource = mockk(relaxed = true)

    private lateinit var pokeRemoteSource: PokeRemoteSource

    @Before
    fun setUp() {
        pokeRemoteSource = PokeRemoteSourceImpl(remoteSource)
    }

    @Test
    fun `verify getPokemon is called`() = runBlocking {
        // Arrange
        val limit = "10"
        val offset = 0

        // Act
        pokeRemoteSource.getPokemon(limit, offset)

        // Assert
        coVerify { remoteSource.getPokemon(limit, offset) }
    }

    @Test
    fun `verify getPokemonDetails is called`() = runBlocking {
        // Arrange
        val pokemonName = "pikachu"

        // Act
        pokeRemoteSource.getPokemonDetails(pokemonName)

        // Assert
        coVerify { remoteSource.getPokemonDetails(pokemonName) }
    }
}
