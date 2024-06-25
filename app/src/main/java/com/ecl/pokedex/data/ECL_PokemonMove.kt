package com.ecl.pokedex.data

import me.sargunvohra.lib.pokekotlin.model.PokemonMove

data class ECL_PokemonMove(val moveId: Int, val moveName: String, val versionGroupDetails: List<ECL_PokemonMoveVersion>) {
    constructor(pokemonMove: PokemonMove) : this (
        pokemonMove.move.id,
        pokemonMove.move.name,
        pokemonMove.versionGroupDetails.let {
            List<ECL_PokemonMoveVersion>(it.size) { pos ->
                ECL_PokemonMoveVersion(
                    it[pos].versionGroup.id,
                    it[pos].moveLearnMethod.name,
                    it[pos].levelLearnedAt
                )
            }
        }
    )

    companion object {
        fun fromList(list: List<PokemonMove>): List<ECL_PokemonMove> {
            return List(list.size) {
                ECL_PokemonMove(list[it])
            }
        }
    }
}

data class ECL_PokemonMoveVersion(
    val versionId: Int,
    val learnMethodName: String,
    val learnLevel: Int
)