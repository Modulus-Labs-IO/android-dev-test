package com.example.pokemonapp.features.pokemon.presentation.list

interface PokemonLisAction {

    data class OnSearchPokemon(
        val keyword: String
    ): PokemonLisAction

}