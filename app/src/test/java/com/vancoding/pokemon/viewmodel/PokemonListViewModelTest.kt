package com.vancoding.pokemon.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vancoding.pokemon.domain.model.Pokemon
import com.vancoding.pokemon.domain.usecases.GetPokemonListUseCase
import com.vancoding.pokemon.domain.usecases.SearchPokemonUseCase
import com.vancoding.pokemon.utils.Constants
import com.vancoding.pokemon.utils.MainCoroutineRule
import com.vancoding.pokemon.utils.NetworkResultState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class PokemonListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var getPokemonListUseCase: GetPokemonListUseCase

    @Mock
    private lateinit var searchPokemonUseCase: SearchPokemonUseCase

    private lateinit var viewModel: PokemonListViewModel

    private val mockPokemonList = listOf(
        Pokemon(1, "bulbasaur", "url"),
        Pokemon(2, "ivysaur", "url")
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        runBlocking {
            whenever(getPokemonListUseCase(anyInt(), anyInt())).thenReturn(
                flowOf(NetworkResultState.Success(mockPokemonList))
            )

            whenever(searchPokemonUseCase(anyString())).thenReturn(
                flowOf(NetworkResultState.Success(listOf(mockPokemonList[0])))
            )
        }

        viewModel = PokemonListViewModel(getPokemonListUseCase, searchPokemonUseCase)
    }

    @Test
    fun `init calls getPokemonListUseCase`() = runTest {
        // When - init is called in setup
        advanceUntilIdle()

        // Then
        verify(getPokemonListUseCase).invoke(Constants.POKEMON_LIST_LIMIT, Constants.CURRENT_PAGE)
    }

    @Test
    fun `loadNextPage increments page and calls getPokemonListUseCase`() = runTest {
        // Given
        val initialPage = Constants.CURRENT_PAGE

        // When
        viewModel.loadNextPage()
        advanceUntilIdle()

        // Then
        verify(getPokemonListUseCase).invoke(Constants.POKEMON_LIST_LIMIT, initialPage + 1)
    }

    @Test
    fun `refreshList resets to first page and loads pokemon list`() = runTest {
        // Given - Load a few pages first
        viewModel.loadNextPage() // Page 1
        viewModel.loadNextPage() // Page 2
        advanceUntilIdle()

        // When
        viewModel.refreshList()
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.isSearchMode.value)
        verify(getPokemonListUseCase, times(2)).invoke(Constants.POKEMON_LIST_LIMIT, Constants.CURRENT_PAGE)
    }

    @Test
    fun `searchPokemon with valid query sets search mode and calls use case`() = runTest {
        // Given
        val query = "bulba"

        // When
        viewModel.searchPokemon(query)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.isSearchMode.value)
        verify(searchPokemonUseCase).invoke(query)
    }

    @Test
    fun `searchPokemon with empty query disables search mode and loads pokemon list`() = runTest {
        // Given - Enable search mode first
        viewModel.searchPokemon("bulba")
        advanceUntilIdle()
        assertTrue(viewModel.isSearchMode.value)

        // When
        viewModel.searchPokemon("")
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.isSearchMode.value)
        verify(getPokemonListUseCase, times(2)).invoke(Constants.POKEMON_LIST_LIMIT, Constants.CURRENT_PAGE)
    }
}