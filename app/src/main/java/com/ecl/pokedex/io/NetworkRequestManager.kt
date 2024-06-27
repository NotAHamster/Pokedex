package com.ecl.pokedex.io

import android.graphics.Bitmap
import com.ecl.pokedex.Globals
import com.ecl.pokedex.helpers.PokemonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.LinkedList

class NetworkRequestManager(var imgSize: Int) {
    private val reqHistory: MutableList<Int> = LinkedList()
    var onReceived: ((PCI_Data)->Unit)? = null

    data class PCI_Data(val pos: Int, val id: Int, val bitmap: Bitmap)

    fun reqData(id: Int, pos: Int): Boolean {
        if (reqHistory.contains(id))
            return false

        reqHistory.add(id)
        CoroutineScope(Dispatchers.IO).launch {
            val pokemon = PokemonUtils(Globals.network.getPokemon(id) ?: return@launch)
            val bitmap = pokemon.imageToBmp(imgSize)
            withContext(Dispatchers.Main) {
                onReceived?.invoke(PCI_Data(pos, id, bitmap))
            }
        }
        return true
    }

    fun clearHistory() = reqHistory.clear()
}