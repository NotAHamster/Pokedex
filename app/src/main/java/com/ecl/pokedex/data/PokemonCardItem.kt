package com.ecl.pokedex.data

import android.graphics.Bitmap

data class PokemonCardItem(val id: Int, var name: String = "Pokemon", var image: Bitmap? = null) {
    fun isImageSet(): Boolean = image != null
}