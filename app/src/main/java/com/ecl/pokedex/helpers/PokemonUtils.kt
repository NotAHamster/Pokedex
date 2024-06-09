package com.ecl.pokedex.helpers

import android.graphics.Bitmap
import android.widget.ImageView
import com.ecl.pokedex.data.PokemonCardItem
import com.ecl.pokedex.PokemonMoveInfo
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

    fun moves(versionGroups: List<Int>): List<PokemonMoveInfo> {
        val moves: MutableList<PokemonMoveInfo> = mutableListOf()

        pokemon.moves.forEach {
            val index = it.versionGroupDetails.indexOfFirst { vgd ->
                versionGroups.any { vg ->
                    vg == vgd.versionGroup.id
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
                vgd.versionGroup.id == versionGroup
            }
            if (index > -1) {
                moves.add(PokemonMoveInfo(it, index))
            }
        }
        return moves.toList()
    }
}