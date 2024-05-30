package com.ecl.pokedex

import android.graphics.Bitmap
import android.widget.ImageView
import com.squareup.picasso.Picasso
import me.sargunvohra.lib.pokekotlin.model.Pokemon
import me.sargunvohra.lib.pokekotlin.model.PokemonMove

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

    fun moves(versionGroups: List<Int>): List<PokemonMove> {
        val moves: MutableList<PokemonMove> = mutableListOf()

        pokemon.moves.forEach {
            if (it.versionGroupDetails.any {
                versionGroups.any { vg ->
                    vg == it.versionGroup.id
                }
            }) {
                moves.add(it)
            }
        }
        return moves.toList()
    }
    fun moves(versionGroup: Int): List<PokemonMove> {
        val moves: MutableList<PokemonMove> = mutableListOf()
        pokemon.moves.forEach {
            if (it.versionGroupDetails.any { vgd ->
                vgd.versionGroup.id == versionGroup
            }) {
                moves.add(it)
            }
        }
        return moves.toList()
    }
}