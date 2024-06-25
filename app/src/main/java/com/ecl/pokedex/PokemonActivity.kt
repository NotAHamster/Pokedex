package com.ecl.pokedex

import android.os.Bundle
import android.widget.TextView
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
import androidx.core.view.children
import com.ecl.pokedex.adapters.RV_MoveListAdapter
import com.ecl.pokedex.adapters.RV_MoveListAdapter.CompareBy
import com.ecl.pokedex.Globals.network
import com.ecl.pokedex.data.ECL_Pokemon
import com.ecl.pokedex.data.ECL_PokemonMove
import com.ecl.pokedex.data.ECL_PokemonSpecies
import com.ecl.pokedex.helpers.PokemonUtils
import com.ecl.pokedex.data.PokemonMoveData
import com.ecl.pokedex.databinding.ActivityPokemonBinding
import com.ecl.pokedex.databinding.MoveListItemBinding
import com.ecl.pokedex.databinding.NavLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPokemonBinding
    private lateinit var pokemonSpecies: ECL_PokemonSpecies
    private lateinit var pokemon: ECL_Pokemon
    private var isGeneration = false
    private var verGroup: Int = -1
    private lateinit var verGroups: List<Int>
    private lateinit var rvMoveListAdapter: RV_MoveListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(Globals.themeID)
        binding = ActivityPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navBinding = NavLayoutBinding.bind(binding.root)
        NavDrawer(this, navBinding)

        CoroutineScope(Dispatchers.IO).launch {
            pokemonSpecies = network.getPokemonSpecies(intent.getIntExtra("speciesId", 1))
            val id = pokemonSpecies.varieties.find { it.isDefault }!!.id
            pokemon = network.getPokemon(id)
            setVersionGroup()

            CoroutineScope(Dispatchers.IO).launch {
                val moveData = getMoveData()
                val levelMoves = moveData.filter {
                    it.learnMethod == "level-up"
                }
                val machineMoves = moveData.filter {
                    it.learnMethod == "machine"
                }
                withContext(Dispatchers.Main) {
                    rvMoveListAdapter = RV_MoveListAdapter()
                    rvMoveListAdapter.replaceDataSet(levelMoves)
                    binding.rvMoveList.adapter = rvMoveListAdapter

                    binding.rvMoveList.viewTreeObserver.addOnGlobalLayoutListener {
                        val maxHeight = binding.svCardContainer.height
                        binding.rvMoveList.apply {
                            if (height > maxHeight) {
                                layoutParams.height = maxHeight
                                requestLayout()
                            }
                        }
                    }

                    val headerBinding: MoveListItemBinding = binding.ilMoveListHeader
                    val headerCallback: ((TextView) -> Unit) = { view ->
                        when (view.text) {
                            "Level" -> rvMoveListAdapter.sort(CompareBy.LearnLevel, true)
                            "Name" -> rvMoveListAdapter.sort(CompareBy.Name, true)
                            "Power" -> rvMoveListAdapter.sort(CompareBy.Power, true)
                            "Acc" -> rvMoveListAdapter.sort(CompareBy.Acc, true)
                            "PP" -> rvMoveListAdapter.sort(CompareBy.PP, true)
                        }
                    }

                    headerBinding.root.children.forEach { child ->
                        if (child is TextView) child.setOnClickListener {
                            headerCallback.invoke(it as TextView)
                        }
                    }
                }
            }

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

    private fun setVersionGroup() {
        val vgID = intent.getIntExtra("versionGroupID", -1)

        if (vgID > -1) {
            verGroup = vgID
            return
        }
        else {
            isGeneration = true
            val genId = intent.getIntExtra("generationID", pokemonSpecies.generationId)
            val verData = network.getGendex(genId).versionGroups
            verGroups = List(verData.size) { verData[it].id }
        }
    }

    private fun getMoveData(): List<PokemonMoveData> {
        val pokemon = PokemonUtils(pokemon)
        val moves = if (isGeneration) {
            pokemon.moves(verGroups)
        }
        else {
            pokemon.moves(verGroup)
        }
        return List(moves.size) {
            lateinit var move: PokemonMoveData
            val pokemonMoveVersion = moves[it].pokemonMove.versionGroupDetails.find { pmv ->
                if (isGeneration) {
                    verGroups.any {vg ->
                        pmv.versionId == vg
                    }
                }
                else {
                    pmv.versionId == verGroup
                }
            }
            network.getMoveData(moves[it].pokemonMove.moveId, true).apply {
                if (pokemonMoveVersion != null) {
                    move = PokemonMoveData(
                        it,
                        name,
                        type,
                        power,
                        acc,
                        pp,
                        pokemonMoveVersion.learnLevel,
                        pokemonMoveVersion.learnMethodName
                    )
                }
            }
            move
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
                val name = when (stat.name) {
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

data class PokemonMoveInfo(
    val pokemonMove: ECL_PokemonMove,
    val versionIndex: Int
)