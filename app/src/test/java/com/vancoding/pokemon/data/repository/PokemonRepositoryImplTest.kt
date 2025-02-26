package com.vancoding.pokemon.data.repository

import com.vancoding.pokemon.data.local.PokemonDatabase
import com.vancoding.pokemon.data.local.dao.PokemonDao
import com.vancoding.pokemon.data.local.dao.PokemonDetailsDao
import com.vancoding.pokemon.data.local.entities.PokemonDetailsEntity
import com.vancoding.pokemon.data.local.entities.PokemonEntity
import com.vancoding.pokemon.data.remote.api.PokeApiService
import com.vancoding.pokemon.data.remote.response.PokemonListItem
import com.vancoding.pokemon.data.remote.response.PokemonListResponse
import com.vancoding.pokemon.utils.NetworkResultState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
class PokemonRepositoryImplTest {

    @Mock
    private lateinit var api: PokeApiService

    @Mock
    private lateinit var database: PokemonDatabase

    @Mock
    private lateinit var pokemonDao: PokemonDao

    @Mock
    private lateinit var pokemonDetailsDao: PokemonDetailsDao

    private lateinit var repository: PokemonRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        `when`(database.pokemonDao()).thenReturn(pokemonDao)
        `when`(database.pokemonDetailsDao()).thenReturn(pokemonDetailsDao)

        repository = PokemonRepositoryImpl(api, database)
    }

    @Test
    fun `getPokemonList emits loading state first`() = runTest {
        // Given
        val limit = 20
        val offset = 0
        val mockPokemonList = listOf<PokemonEntity>()

        `when`(pokemonDao.getPokemonList(limit, offset)).thenReturn(flowOf(mockPokemonList))

        // When
        val result = repository.getPokemonList(limit, offset)

        // Then
        assertTrue(result.first() is NetworkResultState.Loading)
    }

    @Test
    fun `getPokemonList returns cached data when available`() = runTest {
        // Given
        val limit = 20
        val offset = 0
        val cachedPokemons = listOf(
            PokemonEntity(1, "bulbasaur", "url"),
            PokemonEntity(2, "ivysaur", "url"),
        )

        `when`(pokemonDao.getPokemonList(limit, offset)).thenReturn(flowOf(cachedPokemons))

        // When
        val result = repository.getPokemonList(limit, offset)

        // Then - skip Loading state
        result.collect { state ->
            if (state is NetworkResultState.Success) {
                assertEquals(2, state.data.size)
                assertEquals("bulbasaur", state.data[0].name)
                assertEquals("ivysaur", state.data[1].name)
                return@collect
            }
        }
    }

    @Test
    fun `getPokemonList fetches from API when no cache and updates database`() = runTest {
        // Given
        val limit = 20
        val offset = 0

        // Empty database
        `when`(pokemonDao.getPokemonList(limit, offset)).thenReturn(flowOf(emptyList()))

        // Mock API response
        val apiResponseResults = listOf(
            PokemonListItem("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
            PokemonListItem("ivysaur", "https://pokeapi.co/api/v2/pokemon/2/"),
        )
        val apiResponse = PokemonListResponse(count = 2, next = null, previous = null, results = apiResponseResults)
        `when`(api.getPokemonList(limit, offset)).thenReturn(Response.success(apiResponse))

        // When
        val resultFlow = repository.getPokemonList(limit, offset)

        // Then
        var loadingReceived = false
        var successReceived = false

        resultFlow.collect { state ->
            when (state) {
                is NetworkResultState.Loading -> loadingReceived = true
                is NetworkResultState.Success -> {
                    successReceived = true
                    assertEquals(2, state.data.size)
                    assertEquals("bulbasaur", state.data[0].name)

                    // Verify database was updated
                    verify(pokemonDao).insertPokemonList(anyList())

                    return@collect
                }
                else -> {}
            }
        }

        assertTrue(loadingReceived)
        assertTrue(successReceived)
    }

    @Test
    fun `getPokemonList returns failure when API fails and no cache`() = runTest {
        // Given
        val limit = 20
        val offset = 0

        // Empty database
        `when`(pokemonDao.getPokemonList(limit, offset)).thenReturn(flowOf(emptyList()))

        // API fails
        val errorMessage = "Network error"
        `when`(api.getPokemonList(limit, offset)).thenThrow(RuntimeException(errorMessage))

        // When
        val resultFlow = repository.getPokemonList(limit, offset)

        // Then
        resultFlow.collect { state ->
            if (state is NetworkResultState.Failure) {
                assertEquals(errorMessage, state.message)
                return@collect
            }
        }
    }

    @Test
    fun `getPokemonDetails emits loading state first`() = runTest {
        // Given
        val pokemonId = 1
        `when`(pokemonDetailsDao.getPokemonDetails(pokemonId)).thenReturn(flowOf(null))

        // When
        val result = repository.getPokemonDetails(pokemonId)

        // Then
        assertTrue(result.first() is NetworkResultState.Loading)
    }

    @Test
    fun `getPokemonDetails returns cached data when available and not expired`() = runTest {
        // Given
        val pokemonId = 1
        val currentTime = System.currentTimeMillis()
        val cachedDetails = PokemonDetailsEntity(
            id = 1,
            name = "bulbasaur",
            height = 7,
            weight = 69,
            imageUrl = "url",
            typesAsString = "grass,poison",
            abilitiesAsString = "overgrow,chlorophyll",
            baseExperience = 64,
            statsAsString = "hp:45,attack:49",
            lastFetchedTimestamp = currentTime,
        )

        `when`(pokemonDetailsDao.getPokemonDetails(pokemonId)).thenReturn(flowOf(cachedDetails))

        // When
        val result = repository.getPokemonDetails(pokemonId)

        // Then
        result.collect { state ->
            if (state is NetworkResultState.Success) {
                assertEquals("bulbasaur", state.data.name)
                assertEquals(7, state.data.height)
                assertEquals(69, state.data.weight)
                assertEquals("url", state.data.imageUrl)
                assertEquals(2, state.data.types.size)
                assertEquals("grass", state.data.types[0])
                assertEquals("poison", state.data.types[1])
                assertEquals(2, state.data.abilities.size)
                assertEquals("overgrow", state.data.abilities[0])
                assertEquals("chlorophyll", state.data.abilities[1])
                assertEquals(64, state.data.baseExperience)
                assertEquals(2, state.data.stats.size)
                assertEquals(45, state.data.stats["hp"])
                assertEquals(49, state.data.stats["attack"])
                return@collect
            }
        }
    }
}