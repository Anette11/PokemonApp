package com.example.pokemonapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.pokemonapp.R
import com.example.pokemonapp.databinding.ActivityPokemonBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPokemonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_PokemonApp)
        binding = ActivityPokemonBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolBar)
        setNavigation()
    }

    private fun setNavigation() {
        with(binding) {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.fragment_container_view) as NavHostFragment

            val navController = navHostFragment.navController
            val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

            NavigationUI.setupWithNavController(
                navigationView,
                navHostFragment.navController
            )

            NavigationUI.setupWithNavController(
                toolBar,
                navHostFragment.navController,
                appBarConfiguration
            )
        }
    }

    override fun onBackPressed() =
        with(binding) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        }
}