package com.ecl.pokedex

data class PokemonMoveData(
    val id: Int,
    val name: String,
    val type: String,
    val power: Int?,
    val acc: Int?,
    val pp: Int?,
    val levelLearnedAt: Int,
    val learnMethod: String
)