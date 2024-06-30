package com.ecl.pokedex.data

import me.sargunvohra.lib.pokekotlin.model.Generation

class ECL_Generation(
    val id: Int,
    val name: String,
    var pokemonSpecies: List<ECL_NAPI_Resource>,
    val versionGroups: List<ECL_NAPI_Resource>
) {

    constructor(generation: Generation): this(
        generation.id,
        generation.name,
        List(generation.pokemonSpecies.size) {
            ECL_NAPI_Resource(generation.pokemonSpecies[it])
        },
        List(generation.versionGroups.size) {
            ECL_NAPI_Resource(generation.versionGroups[it])
        }
    )

    fun sort() {
        pokemonSpecies = pokemonSpecies.sortedBy {
            it.id
        }
    }
}