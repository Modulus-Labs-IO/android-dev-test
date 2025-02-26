package com.example.android_dev_test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android_dev_test.data.network.PokemonRetrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testAPI();
    }

    private fun testAPI() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Fetch the list
                val pokemonList = PokemonRetrofit.pokemonApiService.getPokemonList()
                println("Pokemon List: ${pokemonList.results}")

                // Fetch details
                val firstPokemonName = pokemonList.results[0].name
                val pokemonDetails = PokemonRetrofit.pokemonApiService.getPokemonDetails(firstPokemonName)
                println(" Details: $pokemonDetails")
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }
}