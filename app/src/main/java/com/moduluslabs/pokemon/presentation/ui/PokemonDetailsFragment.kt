package com.moduluslabs.pokemon.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.moduluslabs.pokemon.databinding.FragmentPokemonDetailsBinding
import com.moduluslabs.pokemon.presentation.viewmodel.PokeViewModel
import com.moduluslabs.pokemon.utils.Constants
import com.moduluslabs.pokemon.utils.ResourceState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PokemonDetailsFragment : Fragment() {

    private val pokemonViewModel: PokeViewModel by viewModels()
    private lateinit var binding: FragmentPokemonDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPokemonDetailsBinding.bind(view)

        fetchPokemonDetails()
    }

    private fun fetchPokemonDetails() {
        val args : PokemonDetailsFragmentArgs by navArgs()
        val poke = args.selectedPoke

        val imageUrl = "${Constants.IMAGE_URL}${poke.name}.jpg"

        Glide.with(binding.root.context)
            .load(imageUrl)
            .into(binding.pokemonImage)

        pokemonViewModel.fetchPokemonDetails(poke.name)

        lifecycleScope.launch {
            pokemonViewModel.pokemonDetailState.collect { state ->
                when (state) {
                    is ResourceState.Loading -> {

                    }
                    is ResourceState.Success -> {

                        binding.pokemonName.text = poke.name
                        binding.actvHeight.text = state.data?.height
                        binding.actvWeight.text = state.data?.weight
                        binding.actvBaseExp.text = state.data?.baseExp
                    }
                    is ResourceState.Error -> {

                    }
                }
            }
        }
    }

}