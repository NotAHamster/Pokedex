package com.ecl.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import com.ecl.pokedex.databinding.ActivityMainBinding
import com.ecl.pokedex.io.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.sargunvohra.lib.pokekotlin.model.Pokemon

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val network = Network()
    private lateinit var rvPokedexAdapter: RV_PokedexAdapter
    //private var themeId: Int = R.style.Base_Theme_Pokedex

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(Theme.id)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup the action bar
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Handle navigation item clicks
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item clicks here
            // For example, navigate to a different fragment/activity based on the clicked item
            when (menuItem.itemId) {
                R.id.nav_item1 -> {
                    // Handle navigation item 1 click
                    Theme.id = R.style.Base_Theme_Pokedex
                    recreate()
                }
                R.id.nav_item2 -> {
                    // Handle navigation item 2 click
                    Theme.id = R.style.Theme_Pokedex_Red
                    recreate()
                }
                // Add more cases as needed
            }
            // Close the drawer after handling the click
            binding.drawerLayout.closeDrawers()
            true
        }

        CoroutineScope(Dispatchers.IO).launch {
            val entries = network.getPokemonSpeciesList(0, 1).results

            withContext(Dispatchers.Main) {
                val pokemonCards = PokemonListUtils(entries).getCards()

                val size = binding.root.width / 3
                rvPokedexAdapter = RV_PokedexAdapter(pokemonCards, this@MainActivity, network, size)
                binding.rvPokedex.adapter = rvPokedexAdapter
            }
        }
    }

    override fun onPause() {
        super.onPause()
        network.onPause()
    }
}