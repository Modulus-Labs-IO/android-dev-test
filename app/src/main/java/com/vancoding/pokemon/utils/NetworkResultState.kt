package com.vancoding.pokemon.utils

sealed class NetworkResultState<T> {
    data class Success<T>(val data: T) : NetworkResultState<T>()
    data class Failure<T>(val message: String) : NetworkResultState<T>()
    class Loading<T> : NetworkResultState<T>()
}