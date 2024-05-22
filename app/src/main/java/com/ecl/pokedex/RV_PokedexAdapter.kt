package com.ecl.pokedex

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import java.util.LinkedList

class RV_PokedexAdapter(
    val dataset: List<PokemonCardItem>,
    imgSize: Int,
    context: Context
) : RecyclerView.Adapter<RV_PokedexAdapter.ViewHolder>() {
    private val defaultImg: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.card_foreground)

    val NRM = NetworkRequestManager(imgSize)
    init {
        NRM.onReceived = { pciData: NetworkRequestManager.PCI_Data ->
            dataset[pciData.pos].apply {
                image = pciData.bitmap
                name = pciData.name
            }
            notifyItemChanged(pciData.pos)
        }
    }

    fun requestNewData(fPos: Int, lPos: Int) {
        for (i in fPos .. lPos) {
            NRM.reqData(dataset[i].id, i)
        }
    }

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
        holder.pc_TextView.text = dataset[position].name
        if (dataset[position].isImageSet())
            holder.pc_ImageView.setImageBitmap(dataset[position].image)
        else
            holder.pc_ImageView.setImageBitmap(defaultImg)
    }

    override fun getItemCount() = dataset.size
}

class NetworkRequestManager(var imgSize: Int) {
    private val reqQueue: MutableList<Int> = LinkedList()
    var onReceived: ((PCI_Data)->Unit)? = null

    data class PCI_Data(val pos: Int, val name: String, val bitmap: Bitmap)

    fun reqData(id: Int, pos: Int): Boolean {
        if (reqQueue.contains(id))
            return false

        reqQueue.add(id)
        CoroutineScope(Dispatchers.IO).launch {
            val pokemon = PokemonUtils(network.getPokemon(id))
            val bitmap = pokemon.imageToBmp(imgSize)
            withContext(Dispatchers.Main) {
                onReceived?.invoke(PCI_Data(pos, pokemon.name(), bitmap))
            }
        }
        return true
    }
}