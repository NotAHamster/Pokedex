package com.ecl.pokedex

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ecl.pokedex.Globals.network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.sargunvohra.lib.pokekotlin.model.Pokemon
import java.util.LinkedList

class RV_PokedexAdapter(
    var dataset: List<PokemonCardItem>,
    imgSize: Int,
    context: Context
) : RecyclerView.Adapter<RV_PokedexAdapter.ViewHolder>() {
    private val defaultImg: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.card_foreground)
    var cardClick: OnClickEvent? = null

    val NRM = NetworkRequestManager(imgSize)
    init {
        NRM.onReceived = { pciData: NetworkRequestManager.PCI_Data ->
            dataset[pciData.pos].apply {
                if (id != pciData.id)
                    return@apply
                image = pciData.bitmap
                notifyItemChanged(pciData.pos)
            }
        }
    }

    fun requestNewData(fPos: Int, lPos: Int) {
        for (i in fPos .. lPos) {
            NRM.reqData(dataset[i].id, i)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun swapNewData(newData: List<PokemonCardItem>) {
        NRM.clearHistory()
        dataset = newData
        notifyDataSetChanged()
    }

    inner class OnClickEvent(private val activity: Activity) {
        fun invoke(position: Int) {
            val cardData = dataset[position]

            CoroutineScope(Dispatchers.IO).launch {
                val pokemonData: Pokemon =
                    network.getPokemon(cardData.name) ?: network.getPokemon(cardData.id)

                //val pokemonSpecies = network.getPokemonSpecies(pokemonData.species.id)
                withContext(Dispatchers.Main) {
                    val intent = Intent(activity, PokemonActivity::class.java)
                    intent.putExtra("speciesId", pokemonData.species.id)
                    activity.startActivity(intent)
                }
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pc_ImageView: ImageView
        val pc_TextView: TextView
        private val cardView: CardView

        init {
            pc_ImageView = view.findViewById(R.id.iv_cardPokemon)
            pc_TextView = view.findViewById(R.id.tv_cardPokemonName)
            cardView = view.findViewById(R.id.cv_pokemon)

            cardView.setOnClickListener {
                cardClick?.invoke(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_pokemon, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pc_TextView.text = dataset[position].name
        if (dataset[position].isImageSet())
            holder.pc_ImageView.setImageBitmap(dataset[position].image)
        else
            holder.pc_ImageView.setImageBitmap(defaultImg)
    }

    override fun getItemCount() = dataset.size
}

class NetworkRequestManager(var imgSize: Int) {
    private val reqHistory: MutableList<Int> = LinkedList()
    var onReceived: ((PCI_Data)->Unit)? = null

    data class PCI_Data(val pos: Int, val id: Int, val bitmap: Bitmap)

    fun reqData(id: Int, pos: Int): Boolean {
        if (reqHistory.contains(id))
            return false

        reqHistory.add(id)
        CoroutineScope(Dispatchers.IO).launch {
            val pokemon = PokemonUtils(network.getPokemon(id))
            val bitmap = pokemon.imageToBmp(imgSize)
            withContext(Dispatchers.Main) {
                onReceived?.invoke(PCI_Data(pos, id, bitmap))
            }
        }
        return true
    }

    fun clearHistory() = reqHistory.clear()
}