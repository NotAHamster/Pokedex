package com.ecl.pokedex

import me.sargunvohra.lib.pokekotlin.model.Generation
import me.sargunvohra.lib.pokekotlin.model.NamedApiResource

class GenerationUtils(generation: Generation) {
    val name: String
    var pokemon: List<NamedApiResource>
    val id: Int

    init {
        name = generation.name
        pokemon = generation.pokemonSpecies
        id = generation.id
    }

    fun sort() {
        pokemon = pokemon.sortedBy {
            it.id
        }
    }
}