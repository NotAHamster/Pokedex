package com.ecl.pokedex

import android.graphics.Bitmap
import android.widget.ImageView
import com.squareup.picasso.Picasso
import me.sargunvohra.lib.pokekotlin.model.Pokemon

class PokemonUtils(private val pokemon: Pokemon) {
    fun imageInto(imageView: ImageView, size: Int) {
        Picasso.get().load(pokemon.sprites.frontDefault).resize(size, size).into(imageView)
    }

    fun imageToBmp(size: Int): Bitmap {
        return Picasso.get().load(pokemon.sprites.frontDefault).resize(size, size).get()
    }

    fun toCard(): PokemonCardItem {
        return PokemonCardItem(pokemon.id, pokemon.name)
    }

    fun name(): String {
        return pokemon.name.replaceFirstChar { it.uppercaseChar() }
    }
}