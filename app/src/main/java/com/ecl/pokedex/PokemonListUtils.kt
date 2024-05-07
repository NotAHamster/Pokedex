package com.ecl.pokedex

import me.sargunvohra.lib.pokekotlin.model.NamedApiResource

class PokemonListUtils(val namedApiResourceList: List<NamedApiResource>) {

    fun getCards(): List<CardPokemon> {
        return List(
            namedApiResourceList.size,
            init = {index -> CardPokemon(namedApiResourceList[index].id) }
        )
    }
}