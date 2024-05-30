package com.ecl.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ecl.pokedex.Globals.network
import com.ecl.pokedex.databinding.ActivityMainBinding
import com.ecl.pokedex.databinding.NavLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvPokedexAdapter: RV_PokedexAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(Globals.themeID)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navBinding = NavLayoutBinding.bind(binding.root)
        NavDrawer(this, navBinding)

        CoroutineScope(Dispatchers.IO).launch {
            val entries = network.getPokemonSpeciesList(0, 30).results

            withContext(Dispatchers.Main) {
                val pokemonCards = PokemonListUtils(entries).getCardItems()

                val size = binding.root.width / 3
                rvPokedexAdapter = RV_PokedexAdapter(pokemonCards, size, this@MainActivity)
                binding.rvPokedex.adapter = rvPokedexAdapter
                binding.rvPokedex.addOnScrollListener(RV_PokedexScrollListener(rvPokedexAdapter))
                rvPokedexAdapter.cardClick = rvPokedexAdapter.OnClickEvent(this@MainActivity)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        network.onPause()
    }
}