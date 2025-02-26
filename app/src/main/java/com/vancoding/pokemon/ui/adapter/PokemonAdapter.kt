package com.vancoding.pokemon.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vancoding.pokemon.R
import com.vancoding.pokemon.databinding.ItemPokemonBinding
import com.vancoding.pokemon.domain.model.Pokemon
import com.vancoding.pokemon.utils.Constants.POKEMON_SPRITE_BASE_URL

class PokemonAdapter(
    private val onPokemonItemClick: (Pokemon) -> Unit,
) : ListAdapter<Pokemon, PokemonAdapter.PokemonViewHolder>(PokemonDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = getItem(position)
        holder.bind(pokemon)
    }

    inner class PokemonViewHolder(
        private val binding: ItemPokemonBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onPokemonItemClick(getItem(position))
                }
            }
        }

        fun bind(pokemon: Pokemon) {
            binding.pokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }

            val formattedId = pokemon.id.toString().padStart(3, '0')
            binding.pokemonId.text = binding.root.context.getString(R.string.pokemon_id, formattedId)

            val imageUrl = "$POKEMON_SPRITE_BASE_URL${pokemon.id}.png"
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_pokemon_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .into(binding.pokemonImage)
        }
    }

    class PokemonDiffCallback : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem == newItem
        }
    }
}