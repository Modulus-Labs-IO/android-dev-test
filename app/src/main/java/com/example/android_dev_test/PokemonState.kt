package com.example.android_dev_test

import com.example.android_dev_test.data.model.PokemonResponse

sealed class PokemonState {
    object Loading : PokemonState()
    data class Success(val data: PokemonResponse) : PokemonState()
    data class Error(val message: String) : PokemonState()
}