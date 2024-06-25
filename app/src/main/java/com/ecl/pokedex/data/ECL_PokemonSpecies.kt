package com.ecl.pokedex.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ecl.pokedex.data.converters.SpeciesVarietiesConverter
import me.sargunvohra.lib.pokekotlin.model.PokemonSpecies
import me.sargunvohra.lib.pokekotlin.model.PokemonSpeciesVariety

@Entity(tableName = "pokemonSpecies")
data class ECL_PokemonSpecies(
    @PrimaryKey
    val id: Int,
    @TypeConverters(SpeciesVarietiesConverter::class)
    val varieties: List<Variety>,
    val generationId: Int
) {
    constructor(pokemonSpecies: PokemonSpecies) : this(
        pokemonSpecies.id,
        Variety.fromList(pokemonSpecies.varieties),
        pokemonSpecies.generation.id
    )
}

data class Variety(val id: Int, val name: String, val isDefault: Boolean) {
    constructor(psv: PokemonSpeciesVariety) : this (psv.pokemon.id, psv.pokemon.name, psv.isDefault)

    companion object {
        fun fromList(psvs: List<PokemonSpeciesVariety>): List<Variety> {
            return List(psvs.size) {
                Variety(psvs[it])
            }
        }
    }
}