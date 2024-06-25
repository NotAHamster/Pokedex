package com.ecl.pokedex.helpers

import android.graphics.Bitmap
import android.widget.ImageView
import com.ecl.pokedex.data.PokemonCardItem
import com.ecl.pokedex.PokemonMoveInfo
import com.ecl.pokedex.data.ECL_Pokemon
import com.squareup.picasso.Picasso

class PokemonUtils(private val pokemon: ECL_Pokemon) {
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

    fun moves(versionGroups: List<Int>): List<PokemonMoveInfo> {
        val moves: MutableList<PokemonMoveInfo> = mutableListOf()

        pokemon.moves.forEach {
            val index = it.versionGroupDetails.indexOfFirst { vgd ->
                versionGroups.any { vg ->
                    vg == vgd.versionId
                }
            }
            if (index > -1) {
                moves.add(PokemonMoveInfo(it, index))
            }
        }
        return moves.toList()
    }
    fun moves(versionGroup: Int): List<PokemonMoveInfo> {
        val moves: MutableList<PokemonMoveInfo> = mutableListOf()
        pokemon.moves.forEach {
            val index = it.versionGroupDetails.indexOfFirst { vgd ->
                vgd.versionId == versionGroup
            }
            if (index > -1) {
                moves.add(PokemonMoveInfo(it, index))
            }
        }
        return moves.toList()
    }
}