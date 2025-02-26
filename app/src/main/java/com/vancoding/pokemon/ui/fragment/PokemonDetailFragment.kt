package com.vancoding.pokemon.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.vancoding.pokemon.R
import com.vancoding.pokemon.base.BaseFragment
import com.vancoding.pokemon.databinding.FragmentPokemonDetailBinding
import com.vancoding.pokemon.domain.model.PokemonDetails
import com.vancoding.pokemon.utils.Constants.KEY_POKEMON_ID
import com.vancoding.pokemon.utils.Constants.KEY_POKEMON_NAME
import com.vancoding.pokemon.utils.Constants.POKEMON_MAX_BASE_STAT_VALUE
import com.vancoding.pokemon.utils.NetworkResultState
import com.vancoding.pokemon.viewmodel.PokemonDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonDetailFragment : BaseFragment<FragmentPokemonDetailBinding>() {

    private val viewModel: PokemonDetailViewModel by viewModels()
    private var pokemonId: Int = 0
    private var pokemonName: String = ""

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentPokemonDetailBinding {
        return FragmentPokemonDetailBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        extractArguments()
        displayBasicInfo()
        setupToolbar()
        loadPokemonDetails()
    }

    private fun extractArguments() {
        arguments?.let { args ->
            pokemonId = args.getInt(KEY_POKEMON_ID, 0)
            pokemonName = args.getString(KEY_POKEMON_NAME, "")
        }
    }

    private fun setupToolbar() {
        val formattedName = pokemonName.replaceFirstChar { it.uppercase() }
        binding.toolbar.tvTitle.text = formattedName
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = formattedName
        binding.toolbar.viewBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun displayBasicInfo() {
        binding.pokemonName.text = pokemonName.replaceFirstChar { it.uppercase() }
        binding.pokemonId.text = getString(R.string.pokemon_id, pokemonId.toString().padStart(3, '0'))
    }

    private fun loadPokemonDetails() {
        viewModel.getPokemonDetails(pokemonId)
        observePokemonDetails()
    }

    private fun observePokemonDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pokemonDetails.collectLatest { state ->
                handlePokemonDetailsState(state)
            }
        }
    }

    private fun handlePokemonDetailsState(state: NetworkResultState<PokemonDetails>) {
        binding.apply {
            when (state) {
                is NetworkResultState.Loading -> {
                    progressBar.isVisible = true
                    errorText.isVisible = false
                }
                is NetworkResultState.Success -> {
                    progressBar.isVisible = false
                    errorText.isVisible = false
                    displayPokemonDetails(state.data)
                }
                is NetworkResultState.Failure -> {
                    progressBar.isVisible = false
                    showError(state.message, errorText)
                }
            }
        }
    }

    private fun displayPokemonDetails(pokemonDetails: PokemonDetails) {
        Glide.with(this)
            .load(pokemonDetails.imageUrl)
            .placeholder(R.drawable.ic_pokemon_placeholder)
            .error(R.drawable.ic_error_placeholder)
            .into(binding.pokemonImage)

        binding.apply {
            pokemonHeight.text = getString(R.string.pokemon_height_format, pokemonDetails.height / 10.0)
            pokemonWeight.text = getString(R.string.pokemon_weight_format, pokemonDetails.weight / 10.0)
            pokemonBaseXp.text = pokemonDetails.baseExperience.toString()
        }

        displayTypes(pokemonDetails.types)
        displayAbilities(pokemonDetails.abilities)
        displayStats(pokemonDetails.stats)
    }

    private fun displayTypes(types: List<String>) {
        binding.typeChipGroup.removeAllViews()
        types.forEach { typeName ->
            Chip(requireContext()).apply {
                text = typeName.replaceFirstChar { it.uppercase() }
                isClickable = false
                chipBackgroundColor = getTypeColor(typeName)
                setTextColor(resources.getColor(android.R.color.white, null))
                binding.typeChipGroup.addView(this)
            }
        }
    }

    private fun displayAbilities(abilities: List<String>) {
        binding.abilitiesChipGroup.removeAllViews()
        abilities.forEach { abilityName ->
            Chip(requireContext()).apply {
                text = abilityName.replace('-', ' ').replaceFirstChar { it.uppercase() }
                isClickable = false
                binding.abilitiesChipGroup.addView(this)
            }
        }
    }

    private fun displayStats(stats: Map<String, Int>) {
        binding.statsContainer.removeAllViews()
        val layoutInflater = LayoutInflater.from(requireContext())

        stats.forEach { (statName, statValue) ->
            layoutInflater.inflate(R.layout.item_stat, binding.statsContainer, false).apply {
                findViewById<TextView>(R.id.stat_name).text = formatStatName(statName)
                findViewById<TextView>(R.id.stat_value).text = getString(R.string.stat_value_format, statValue)

                findViewById<View>(R.id.stat_progress).apply {
                    layoutParams = layoutParams.apply {
                        width = (statValue.toFloat() / POKEMON_MAX_BASE_STAT_VALUE * (resources.displayMetrics.widthPixels * 0.5)).toInt()
                    }
                }

                binding.statsContainer.addView(this)
            }
        }
    }

    private fun formatStatName(name: String): String {
        return when (name) {
            "hp" -> "HP"
            "attack" -> "Attack"
            "defense" -> "Defense"
            "special-attack" -> "Sp. Atk"
            "special-defense" -> "Sp. Def"
            "speed" -> "Speed"
            else -> name.replaceFirstChar { it.uppercase() }
        }
    }

    private fun getTypeColor(type: String): android.content.res.ColorStateList {
        val colorResId = when (type.lowercase()) {
            "normal" -> android.R.color.darker_gray
            "fire" -> R.color.pokemon_type_fire
            "water" -> R.color.pokemon_type_water
            "electric" -> R.color.pokemon_type_electric
            "grass" -> R.color.pokemon_type_grass
            "ice" -> R.color.pokemon_type_ice
            "fighting" -> R.color.pokemon_type_fighting
            "poison" -> R.color.pokemon_type_poison
            "ground" -> R.color.pokemon_type_ground
            "flying" -> R.color.pokemon_type_flying
            "psychic" -> R.color.pokemon_type_psychic
            "bug" -> R.color.pokemon_type_bug
            "rock" -> R.color.pokemon_type_rock
            "ghost" -> R.color.pokemon_type_ghost
            "dragon" -> R.color.pokemon_type_dragon
            "dark" -> R.color.pokemon_type_dark
            "steel" -> R.color.pokemon_type_steel
            "fairy" -> R.color.pokemon_type_fairy
            else -> android.R.color.darker_gray
        }

        return android.content.res.ColorStateList.valueOf(resources.getColor(colorResId, null))
    }
}