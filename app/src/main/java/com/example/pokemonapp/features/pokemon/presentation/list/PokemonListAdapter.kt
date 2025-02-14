package com.example.pokemonapp.features.pokemon.presentation.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokemonapp.databinding.PokemonListItemBinding
import com.example.pokemonapp.features.pokemon.domain.model.PokemonListItem

class PokemonListAdapter(
    val context: Context
) : PagingDataAdapter<PokemonListItem, PokemonListAdapter.PokemonViewHolder>(DIFF_CALLBACK) {

    private val pokemonList = mutableListOf<PokemonListItem>()
    private var onItemClick: ((PokemonListItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding: PokemonListItemBinding = PokemonListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = getItem(position)
        if (pokemon != null) {
            holder.bind(pokemon)
        }
    }

    override fun getItemCount(): Int {
        return this.snapshot().items.size
    }

    inner class PokemonViewHolder(private val binding: PokemonListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemon: PokemonListItem) {
            binding.pokemonName.text = pokemon.name
            val number = if(pokemon.url.endsWith("/")) {
                pokemon.url.dropLast(1).takeLastWhile { it.isDigit() }
            } else {
                pokemon.url.takeLastWhile { it.isDigit() }
            }
            val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
            Glide.with(context).load(url).into(binding.pokemonImage)
            itemView.setOnClickListener {
                onItemClick?.invoke(pokemon)
            }
        }
    }

    fun setOnItemClickListener(listener: (PokemonListItem) -> Unit) {
        this.onItemClick = listener
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PokemonListItem>() {
            override fun areItemsTheSame(oldItem: PokemonListItem, newItem: PokemonListItem): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: PokemonListItem, newItem: PokemonListItem): Boolean =
                oldItem == newItem
        }
    }
}