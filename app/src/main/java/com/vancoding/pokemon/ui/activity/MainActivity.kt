package com.vancoding.pokemon.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.vancoding.pokemon.R
import com.vancoding.pokemon.databinding.ActivityMainBinding
import com.vancoding.pokemon.utils.hide
import com.vancoding.pokemon.utils.show
import com.vancoding.pokemon.viewmodel.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel: PokemonListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.pokemonListFragment)
        )

        setupNavigation()
        setupAppBarConfiguration()
        setupSearch()
        setupNavListener()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupAppBarConfiguration() {
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.pokemonListFragment)
        )
    }

    private fun setupNavListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.pokemonListFragment -> {
                    binding.header.show()
                    binding.appBar.show()
                }
                R.id.pokemonDetailFragment -> {
                    binding.header.hide()
                    binding.appBar.hide()
                    hideKeyboard()
                }
            }
        }
    }

    private fun setupSearch() {
        binding.tvSearch.setOnEditorActionListener { _, _, _ ->
            val searchText = binding.tvSearch.text.toString()
            viewModel.searchPokemon(searchText)
            hideKeyboard()
            true
        }

        binding.tvSearch.addTextChangedListener(afterTextChanged = { editable ->
            editable?.let {
                viewModel.searchPokemon(it.toString())
            }
        })
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.tvSearch.windowToken, 0)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}