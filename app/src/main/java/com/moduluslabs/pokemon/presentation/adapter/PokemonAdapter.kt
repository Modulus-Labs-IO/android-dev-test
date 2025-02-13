package com.moduluslabs.pokemon.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moduluslabs.pokemon.databinding.ItemPokemonBinding
import com.moduluslabs.pokemon.domain.model.PokeResult
import com.moduluslabs.pokemon.utils.Constants
import javax.inject.Inject

class PokemonAdapter @Inject constructor() : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<PokeResult>() {
        override fun areItemsTheSame(oldItem: PokeResult, newItem: PokeResult): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: PokeResult, newItem: PokeResult): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    private var onPokeItemClickListener: ((PokeResult) -> Unit)? = null

    fun pokeItemClickListener(listener: (PokeResult) -> Unit) {
        onPokeItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = differ.currentList[position]
        holder.bind(pokemon)
    }

    inner class PokemonViewHolder(private val binding: ItemPokemonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemon: PokeResult) {
            binding.pokemonName.text = pokemon.name.uppercase()

            val imageUrl = "${Constants.IMAGE_URL}${pokemon.name}.jpg"

            Glide.with(binding.root.context)
                .load(imageUrl)
                .into(binding.pokemonImage)


            binding.root.setOnClickListener {
                onPokeItemClickListener?.invoke(pokemon)
            }
        }
    }
}
