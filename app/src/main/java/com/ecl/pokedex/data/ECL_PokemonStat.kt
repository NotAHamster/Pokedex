package com.ecl.pokedex.data

import me.sargunvohra.lib.pokekotlin.model.PokemonStat

data class ECL_PokemonStat (
    val name: String,
    val baseStat: Int
) {
    constructor(stat: PokemonStat): this(
        stat.stat.name,
        stat.baseStat
    )

    companion object {
        fun fromList(stats: List<PokemonStat>): List<ECL_PokemonStat> {
            return List(stats.size) {
                ECL_PokemonStat(stats[it])
            }
        }
    }
}