package com.moduluslabs.pokemon.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moduluslabs.pokemon.R
import com.moduluslabs.pokemon.databinding.FragmentPokemonBinding
import com.moduluslabs.pokemon.presentation.adapter.PokemonAdapter
import com.moduluslabs.pokemon.presentation.viewmodel.PokeViewModel
import com.moduluslabs.pokemon.utils.ResourceState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PokemonFragment : Fragment() {

    private val pokemonViewModel: PokeViewModel by viewModels()
    @Inject
    lateinit var pokemonAdapter: PokemonAdapter
    private lateinit var binding: FragmentPokemonBinding
    private var offset = 0
    private val limit = 10

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPokemonBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPokemonBinding.bind(view)

        binding.rvPoke.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvPoke.adapter = pokemonAdapter

        fetchPokemon()

        binding.rvPoke.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (totalItemCount <= (lastVisibleItemPosition + 5)) {
                    loadMorePokemon()
                }
            }
        })

        pokemonAdapter.pokeItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("selected_poke", it)
            }

            findNavController().navigate(
                R.id.action_pokemonListFragment_to_pokemonDetailsFragment, bundle
            )
        }
    }

    private fun fetchPokemon() {
        pokemonViewModel.fetchPokemon(limit.toString(), offset)

        lifecycleScope.launch {
            pokemonViewModel.pokemonState.collect { state ->
                when (state) {
                    is ResourceState.Loading -> {
                        binding.pbLoading.visibility = View.VISIBLE
                    }
                    is ResourceState.Success -> {
                        binding.pbLoading.visibility = View.GONE
                        val newList = state.data?.results
                        pokemonAdapter.differ.submitList(pokemonAdapter.differ.currentList + (newList ?: emptyList()))

                        newList?.forEach { pokeItem ->
                            pokemonViewModel.savePokemon(pokeItem)
                        }
                    }
                    is ResourceState.Error -> {
                        binding.pbLoading.visibility = View.GONE
                        pokemonViewModel.getSavedPokemon().collect { pokeList ->
                            pokemonAdapter.differ.submitList(pokeList)
                        }
                    }
                }
            }
        }
    }

    private fun loadMorePokemon() {
        offset += limit
        pokemonViewModel.fetchPokemon(limit.toString(), offset)
    }
}
