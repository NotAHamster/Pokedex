package com.ecl.pokedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import com.ecl.pokedex.adapters.RV_PokedexAdapter
import com.ecl.pokedex.Globals.network
import com.ecl.pokedex.data.PokemonCardItem
import com.ecl.pokedex.helpers.PokemonListUtils
import com.ecl.pokedex.databinding.ActivityMainBinding
import com.ecl.pokedex.databinding.NavLayoutBinding
import com.ecl.pokedex.io.CacheListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.PatternSyntaxException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvPokedexAdapter: RV_PokedexAdapter
    private lateinit var pokemonCards: List<PokemonCardItem>
    private var isFiltered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Globals.LocalStorage.set(this)
        CoroutineScope(Dispatchers.IO).launch {
            network.addCacheListener(CacheListener(Globals.LocalStorage.set(this@MainActivity)))
        }
        setTheme(Globals.themeID)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navBinding = NavLayoutBinding.bind(binding.root)
        NavDrawer(this, navBinding)

        CoroutineScope(Dispatchers.IO).launch {
            val entries = PokemonListUtils(network.getPokemonSpeciesList(0, 10000))

            withContext(Dispatchers.Main) {
                //val entries = PokemonListUtils(entries)
                pokemonCards = entries.getCardItems()

                val size = binding.root.width / 3
                rvPokedexAdapter = RV_PokedexAdapter(pokemonCards, size, this@MainActivity)
                binding.rvPokedex.adapter = rvPokedexAdapter
                binding.rvPokedex.addOnScrollListener(RV_PokedexScrollListener(rvPokedexAdapter))
                rvPokedexAdapter.cardClick = rvPokedexAdapter.OnClickEvent(this@MainActivity)

                val names = entries.getPokemonNames()
                val acTextViewAdapter: ArrayAdapter<String> = ArrayAdapter(this@MainActivity, android.R.layout.simple_dropdown_item_1line, names)
                binding.actvPokedex.setAdapter(acTextViewAdapter)
                binding.actvPokedex.threshold = 1
                binding.actvPokedex.setOnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_NEXT ||
                        event?.action == KeyEvent.ACTION_DOWN &&
                        event.keyCode == KeyEvent.KEYCODE_ENTER
                    ) {
                        val inputText = v.text.toString()
                        if (inputText.isEmpty()) {
                            if (isFiltered) {
                                rvPokedexAdapter.swapNewData(pokemonCards)
                                isFiltered = false
                            }
                            return@setOnEditorActionListener false
                        }
                        val filterData: List<PokemonCardItem> = try {
                            val inputRegex = inputText.toRegex(RegexOption.IGNORE_CASE)
                            pokemonCards.filter {
                                inputRegex.containsMatchIn(it.name)
                            }
                        } catch (err: PatternSyntaxException) {
                            pokemonCards.filter {
                                it.name.contains(inputText, true)
                            }
                        }

                        rvPokedexAdapter.swapNewData(filterData)
                        isFiltered = true
                        true
                    }
                    else false
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && data != null) {
            if (data.getBooleanExtra("themeChanged", false)) {
                recreate()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        network.onPause()
    }
}