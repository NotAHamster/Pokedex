package com.ecl.pokedex

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecl.pokedex.adapters.RV_GensAdapter
import com.ecl.pokedex.adapters.RV_PokedexAdapter
import com.ecl.pokedex.Globals.network
//import com.ecl.pokedex.helpers.GenerationUtils
import com.ecl.pokedex.helpers.PokemonListUtils
import com.ecl.pokedex.data.GenItemData
import com.ecl.pokedex.databinding.ActivityGenerationsBinding
import com.ecl.pokedex.databinding.NavLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GenerationsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityGenerationsBinding
    private lateinit var rvPokedexAdapter: RV_PokedexAdapter
    private lateinit var rvScrollListener: RV_PokedexScrollListener
    private lateinit var rvPokedex: RecyclerView
    private lateinit var rvGensadapter: RV_GensAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(Globals.themeID)
        binding = ActivityGenerationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rvPokedex = binding.rvPokedex

        val navBinding = NavLayoutBinding.bind(binding.root)
        val navDrawer = NavDrawer(this, navBinding)
        navDrawer.onCurrentActivityClicked = {
            loadGeneration(it)
            rvGensadapter.setNewActive(rvGensadapter.dataset.indexOfFirst { genItem ->
                genItem.id == it
            })
            true
        }

        binding.rvGens.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val defaultGen = intent.getIntExtra("default-gen", 3)
        CoroutineScope(Dispatchers.IO).launch {
            val data = getRVGensData()

            withContext(Dispatchers.Main) {
                rvGensadapter = RV_GensAdapter(data, theme) { id ->
                    loadGeneration(id)
                }
                rvGensadapter.setNewActive(defaultGen - 1)
                binding.rvGens.adapter = rvGensadapter
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val gen = network.getGendex(defaultGen) ?: return@launch
            gen.sort()

            withContext(Dispatchers.Main) {
                val pokemonCards = PokemonListUtils(gen.pokemonSpecies).getCardItems()

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
            val gen = network.getGendex(id) ?: return@launch
            gen.sort()

            withContext(Dispatchers.Main) {
                val pokemonCards = PokemonListUtils(gen.pokemonSpecies).getCardItems()

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