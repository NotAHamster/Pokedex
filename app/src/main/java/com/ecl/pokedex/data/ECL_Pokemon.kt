package com.ecl.pokedex.data

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ecl.pokedex.PokemonMoveInfo
import com.ecl.pokedex.data.converters.PokemonMovesConverter
import com.ecl.pokedex.data.converters.PokemonStatsConverter
import com.ecl.pokedex.data.converters.SpritesConverter
import com.squareup.picasso.Picasso
import me.sargunvohra.lib.pokekotlin.model.Pokemon
import me.sargunvohra.lib.pokekotlin.model.PokemonSprites

@Entity(tableName = "pokemon")
class ECL_Pokemon(
    @PrimaryKey
    val id: Int,
    name: String,
    val speciesID: Int,
    @TypeConverters(SpritesConverter::class)
    val sprites: PokemonSprites,
    @TypeConverters(PokemonMovesConverter::class)
    val moves: List<ECL_PokemonMove>,
    @TypeConverters(PokemonStatsConverter::class)
    val stats: List<ECL_PokemonStat>) {

    constructor(pokemon: Pokemon) : this(
        id = pokemon.id,
        name = pokemon.name.replaceFirstChar { it.uppercaseChar() },
        speciesID = pokemon.species.id,
        sprites = pokemon.sprites,
        moves = ECL_PokemonMove.fromList(pokemon.moves),
        stats = ECL_PokemonStat.fromList(pokemon.stats)
    )

    val name: String = name.replaceFirstChar { it.uppercaseChar() }

    fun imageInto(imageView: ImageView, size: Int) {
        Picasso.get().load(sprites.frontDefault).resize(size, size).into(imageView)
    }

    fun imageToBmp(size: Int): Bitmap {
        return Picasso.get().load(sprites.frontDefault).resize(size, size).get()
    }

    fun toCard(): PokemonCardItem {
        return PokemonCardItem(id, name)
    }

    @Ignore
    fun name(): String {
        return name.replaceFirstChar { it.uppercaseChar() }
    }

    fun moves(versionGroups: List<Int>): List<PokemonMoveInfo> {
        val moves: MutableList<PokemonMoveInfo> = mutableListOf()

        this.moves.forEach {
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
        this.moves.forEach {
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