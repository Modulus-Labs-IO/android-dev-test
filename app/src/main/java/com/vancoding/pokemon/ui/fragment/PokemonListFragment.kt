package com.vancoding.pokemon.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vancoding.pokemon.R
import com.vancoding.pokemon.base.BaseFragment
import com.vancoding.pokemon.databinding.FragmentPokemonListBinding
import com.vancoding.pokemon.domain.model.Pokemon
import com.vancoding.pokemon.ui.adapter.PokemonAdapter
import com.vancoding.pokemon.utils.Constants.ERROR_UNKNOWN
import com.vancoding.pokemon.utils.Constants.KEY_POKEMON_ID
import com.vancoding.pokemon.utils.Constants.KEY_POKEMON_NAME
import com.vancoding.pokemon.utils.NetworkResultState
import com.vancoding.pokemon.viewmodel.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonListFragment : BaseFragment<FragmentPokemonListBinding>() {

    private lateinit var pokemonAdapter: PokemonAdapter
    private val viewModel: PokemonListViewModel by activityViewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentPokemonListBinding {
        return FragmentPokemonListBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSwipeRefresh()
        observePokemonList()
        observeSearchModeState()
    }

    private fun setupRecyclerView() {
        pokemonAdapter = PokemonAdapter { pokemon ->
            navigateToDetailFragment(pokemon)
        }

        binding.pokemonRecyclerView.apply {
            adapter = pokemonAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(paginationScrollListener)
        }
    }

    private val paginationScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                && firstVisibleItemPosition >= 0
            ) {
                viewModel.loadNextPage()
            }
        }
    }

    private fun navigateToDetailFragment(pokemon: Pokemon) {
        val bundle = Bundle().apply {
            putInt(KEY_POKEMON_ID, pokemon.id)
            putString(KEY_POKEMON_NAME, pokemon.name)
        }
        findNavController().navigate(R.id.action_pokemonListFragment_to_pokemonDetailFragment, bundle)
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshList()
        }
    }

    private fun observePokemonList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pokemonList.collectLatest { state ->
                handlePokemonListState(state)
            }
        }
    }

    private fun observeSearchModeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isSearchMode.collect { isSearching ->
                    binding.swipeRefresh.isEnabled = !isSearching
                }
            }
        }
    }

    private fun handlePokemonListState(state: NetworkResultState<List<Pokemon>>) {
        binding.swipeRefresh.isRefreshing = false

        when (state) {
            is NetworkResultState.Loading -> handleLoadingState()
            is NetworkResultState.Success -> handleSuccessState(state.data)
            is NetworkResultState.Failure -> handleFailureState(state.message)
        }
    }

    private fun handleLoadingState() {
        if (pokemonAdapter.itemCount == 0) {
            binding.progressBar.isVisible = true
        }
        binding.errorText.isVisible = false
    }

    private fun handleSuccessState(data: List<Pokemon>) {
        binding.progressBar.isVisible = false
        binding.errorText.isVisible = false

        if (data.isEmpty() && pokemonAdapter.itemCount == 0) {
            showEmptyState()
        } else {
            pokemonAdapter.submitList(data)
        }
    }

    private fun showEmptyState() {
        binding.errorText.isVisible = true
        binding.errorText.text = getString(R.string.no_pokemon_found)
    }

    private fun handleFailureState(message: String?) {
        binding.progressBar.isVisible = false

        if (pokemonAdapter.itemCount == 0) {
            showError(message ?: ERROR_UNKNOWN, binding.errorText)
        } else {
            showToast("Error: ${message ?: ERROR_UNKNOWN}")
        }
    }
}