package com.ecl.pokedex

import me.sargunvohra.lib.pokekotlin.model.NamedApiResource

class PokemonListUtils(val namedApiResourceList: List<NamedApiResource>) {

    fun getCardItems(): List<PokemonCardItem> {
        return List(
            namedApiResourceList.size,
            init = {index ->
                namedApiResourceList[index].let {
                    PokemonCardItem(it.id, it.name)
                }
            }
        )
    }
}