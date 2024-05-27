package com.ecl.pokedex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.ecl.pokedex.Globals.network
import com.ecl.pokedex.databinding.ActivityPokemonBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.sargunvohra.lib.pokekotlin.model.Pokemon
import me.sargunvohra.lib.pokekotlin.model.PokemonSpecies

class PokemonActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPokemonBinding
    private lateinit var pokemonSpecies: PokemonSpecies
    private lateinit var pokemon: Pokemon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(Globals.themeID)
        binding = ActivityPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NavDrawer(binding.navView, this)

        CoroutineScope(Dispatchers.IO).launch {
            pokemonSpecies = network.getPokemonSpecies(intent.getIntExtra("speciesId", 1))
            val id = pokemonSpecies.varieties.find { it.isDefault }!!.pokemon.id
            pokemon = network.getPokemon(id)

            withContext(Dispatchers.Main) {
                val pokeUtil = PokemonUtils(pokemon)
                pokeUtil.imageInto(binding.ivPokemon, 240)
                binding.tvPokemonName.text = pokeUtil.name()

                val composeView = binding.cvStats
                composeView.setContent {
                    CustomCanvas()
                }
            }
        }
    }

    @Composable
    fun CustomCanvas() {
        val textStyle = TextStyle(Color.Black, fontSize = TextUnit(20f, TextUnitType.Sp))
        val textMeasurer = rememberTextMeasurer()
        val statBuilder = StatBuilder(textMeasurer, textStyle)

        Canvas(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {
            for (stat in pokemon.stats) {
                val name = when (stat.stat.name) {
                    "hp" -> "HP"
                    "attack" -> "Attack"
                    "defense" -> "Defence"
                    "special-attack" -> "SP. Atk"
                    "special-defense" -> "SP. Def"
                    "speed" -> "Speed"
                    else -> "ERR"
                }
                statBuilder.addRow(name, stat.baseStat)
            }
            statBuilder.drawRows(this)
        }
    }
}
