package com.example.pokemonapp.features.pokemon.presentation.list

import android.text.Editable
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pokemonapp.core.base.BaseFragment
import com.example.pokemonapp.databinding.FragmentPokemonListBinding
import com.example.pokemonapp.features.pokemon.presentation.PokemonViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PokemonListFragment : BaseFragment<FragmentPokemonListBinding>(
    FragmentPokemonListBinding::inflate
) {

    private val viewModel: PokemonViewModel by viewModels()
    private lateinit var pokemonListAdapter: PokemonListAdapter

    override fun setupView() {
        super.setupView()
        pokemonListAdapter = PokemonListAdapter(requireContext())
        binding.apply {
            rvPokemonList.apply {
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                adapter = pokemonListAdapter
            }
        }
//        viewModel.onPokemonListAction(PokemonLisAction.OnCheckCachedPokemonsCount)
    }

    override fun observeState() {
        super.observeState()
//        lifecycleScope.launch {
//            lifecycle.currentStateFlow.collectLatest { currentState ->
//                if (currentState.isAtLeast(Lifecycle.State.CREATED)) {
//                    viewModel.uiState.collectLatest { state ->
//                        pokemonListAdapter.submitData(state.pagingData)
//                    }
//                }
//            }
//        }

        lifecycleScope.launch {
            lifecycle.currentStateFlow.collectLatest { currentState ->
                if (currentState.isAtLeast(Lifecycle.State.CREATED)) {
                    viewModel.pokemonPagingData.collectLatest {
                        pokemonListAdapter.submitData(it)
                    }
                }
            }
        }
    }

    override fun setupListeners() {
        super.setupListeners()
        binding.apply {
            pokemonListAdapter.setOnItemClickListener {
                findNavController().navigate(PokemonListFragmentDirections.actionListToDetail(it.name))
            }
            etSearch.addTextChangedListener {
                viewModel.onPokemonListAction(
                    PokemonLisAction.OnSearchPokemon(
                        keyword = it.toString()
                    )
                )
            }
        }
    }

}