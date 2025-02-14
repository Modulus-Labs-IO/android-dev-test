package com.example.pokemonapp.features.pokemon.presentation.details

import androidx.navigation.fragment.navArgs
import com.example.pokemonapp.core.base.BaseFragment
import com.example.pokemonapp.databinding.FragmentPokemonDetailsBinding

class PokemonDetailsFragment: BaseFragment<FragmentPokemonDetailsBinding>(
    FragmentPokemonDetailsBinding::inflate
) {

    private val args by navArgs<PokemonDetailsFragmentArgs>()

    override fun setupView() {
        super.setupView()

    }

    override fun observeState() {
        super.observeState()
    }

}