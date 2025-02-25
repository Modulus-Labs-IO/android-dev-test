package com.vancoding.pokemon.data.remote.config

object ApiConfig {

    const val BASE_URL = "https://pokeapi.co/api/v2/"
    const val TIMEOUT_SECONDS = 30L

    object EndPoints {
        const val POKEMON_LIST = "pokemon"
        const val POKEMON_DETAILS = "pokemon/{id}"
    }
}