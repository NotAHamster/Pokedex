package com.ecl.pokedex

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecl.pokedex.Globals.network
import com.ecl.pokedex.databinding.ActivityGenerationsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GenerationsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityGenerationsBinding
    private lateinit var rvPokedexAdapter: RV_PokedexAdapter
    private lateinit var rvScrollListener: RV_PokedexScrollListener
    private lateinit var rvPokedex: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(Globals.themeID)
        binding = ActivityGenerationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rvPokedex = binding.rvPokedex

        NavDrawer(binding.navView, this)

        binding.rvGens.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val defaultGen = 3
        CoroutineScope(Dispatchers.IO).launch {
            val data = getRVGensData()

            withContext(Dispatchers.Main) {
                val rvGensadapter = RV_GensAdapter(data, theme) { id ->
                    loadGeneration(id)
                }
                rvGensadapter.setNewActive(defaultGen - 1)
                binding.rvGens.adapter = rvGensadapter
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val gen = GenerationUtils(network.getGendex(defaultGen))
            gen.sort()

            withContext(Dispatchers.Main) {
                val pokemonCards = PokemonListUtils(gen.pokemon).getCardItems()

                val size = binding.root.width / 3
                rvPokedexAdapter = RV_PokedexAdapter(pokemonCards, size, this@GenerationsActivity)
                rvPokedex.adapter = rvPokedexAdapter
                rvScrollListener = RV_PokedexScrollListener(rvPokedexAdapter)
                rvPokedex.addOnScrollListener(rvScrollListener)
                rvPokedexAdapter.cardClick = rvPokedexAdapter.OnClickEvent(this@GenerationsActivity)
            }
        }
    }

    private fun getRVGensData(): List<GenItemData> {
        val data = network.getGenerations()
        return List(
            data.size,
            init = {index ->
                data[index].let {
                    GenItemData(it.name.removePrefix("generation-").uppercase(), it.id)
                }
            }
        )
    }

    private fun loadGeneration(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val gen = GenerationUtils(network.getGendex(id))
            gen.sort()

            withContext(Dispatchers.Main) {
                val pokemonCards = PokemonListUtils(gen.pokemon).getCardItems()

                //set new data
                rvPokedexAdapter.swapNewData(pokemonCards)

                //scroll to the top
                rvPokedex.scrollToPosition(0)

                //request the new visible data when updated
                rvPokedex.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        rvPokedex.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        rvScrollListener.onScrolled(rvPokedex, 0, 0)
                    }
                })
            }
        }
    }
}