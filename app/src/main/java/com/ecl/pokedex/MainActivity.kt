package com.ecl.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ecl.pokedex.Globals.network
import com.ecl.pokedex.databinding.ActivityMainBinding
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

        // Setup the action bar
        /*val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()*/

        NavDrawer(binding.navView, this)

        CoroutineScope(Dispatchers.IO).launch {
            val entries = network.getPokemonSpeciesList(0, 30).results

            withContext(Dispatchers.Main) {
                val pokemonCards = PokemonListUtils(entries).getCardItems()

                val size = binding.root.width / 3
                rvPokedexAdapter = RV_PokedexAdapter(pokemonCards, size, this@MainActivity)
                binding.rvPokedex.adapter = rvPokedexAdapter
                binding.rvPokedex.addOnScrollListener(RV_PokedexScrollListener(rvPokedexAdapter))
            }
        }
        /*binding.rvPokedex.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                // Load data for items in the visible range
                rvPokedexAdapter.requestNewData(
                    firstVisibleItemPosition,
                    lastVisibleItemPosition
                )
            }
        })*/
    }

    override fun onPause() {
        super.onPause()
        network.onPause()
    }
}