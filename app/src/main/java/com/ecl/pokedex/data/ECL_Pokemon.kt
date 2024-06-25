package com.ecl.pokedex.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ecl.pokedex.data.converters.PokemonMovesConverter
import com.ecl.pokedex.data.converters.PokemonStatsConverter
import com.ecl.pokedex.data.converters.SpritesConverter
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
}