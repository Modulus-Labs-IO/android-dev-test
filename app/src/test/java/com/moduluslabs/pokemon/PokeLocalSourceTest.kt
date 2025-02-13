package com.moduluslabs.pokemon

import com.moduluslabs.pokemon.data.db.PokeDao
import com.moduluslabs.pokemon.data.repository.datasourceimpl.PokeLocalSourceImpl
import com.moduluslabs.pokemon.domain.model.PokeResult
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PokeLocalSourceTest {

    private val pokeDao: PokeDao = mockk(relaxed = true)

    private lateinit var pokeLocalSource: PokeLocalSourceImpl

    @Before
    fun setUp() {
        pokeLocalSource = PokeLocalSourceImpl(pokeDao)
    }

    @Test
    fun `verify savePokemon is called`() = runBlocking {
        // Arrange
        val pokeResult = PokeResult(name = "Pikachu", url = "")

        // Act
        pokeLocalSource.savePokemon(pokeResult)

        // Assert
        coVerify { pokeDao.savePokemon(pokeResult) }
    }

    @Test
    fun `verify getSavedPokemon is called`() {
        // Act
        pokeLocalSource.getSavedPokemon()

        // Assert
        verify { pokeDao.getSavedPokemon() }
    }
}
