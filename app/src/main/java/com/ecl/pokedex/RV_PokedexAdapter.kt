package com.ecl.pokedex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ecl.pokedex.Globals.network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RV_PokedexAdapter(
    val dataset: List<CardPokemon>,
    private val cardSize: Int
) : RecyclerView.Adapter<RV_PokedexAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pc_ImageView: ImageView
        val pc_TextView: TextView

        init {
            pc_ImageView = view.findViewById(R.id.iv_cardPokemon)
            pc_TextView = view.findViewById(R.id.tv_cardPokemonName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_pokemon, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val pokemon = PokemonUtils(network.getPokemon(dataset[position].id))

            withContext(Dispatchers.Main) {
                holder.pc_TextView.text = pokemon.name()
                pokemon.imageInto(holder.pc_ImageView, cardSize)
            }
        }
    }

    override fun getItemCount() = dataset.size
}