package com.ecl.pokedex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ecl.pokedex.Globals.network
import com.ecl.pokedex.databinding.ActivityGenerationsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GenerationsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityGenerationsBinding
    private lateinit var rvPokedexAdapter: RV_PokedexAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(Globals.themeID)
        binding = ActivityGenerationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NavDrawer(binding.navView, this)

        CoroutineScope(Dispatchers.IO).launch {
            val gen3 = GenerationUtils(network.getGendex(3))
            gen3.sort()

            withContext(Dispatchers.Main) {
                val pokemonCards = PokemonListUtils(gen3.pokemon).getCardItems()

                val size = binding.root.width / 3
                rvPokedexAdapter = RV_PokedexAdapter(pokemonCards, size, this@GenerationsActivity)
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

                if (abs(dy) < 5) {
                    // Load data for items in the visible range
                    rvPokedexAdapter.requestNewData(
                        firstVisibleItemPosition,
                        lastVisibleItemPosition
                    )
                }
            }
        })*/
    }

}