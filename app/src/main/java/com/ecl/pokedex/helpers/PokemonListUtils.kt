package com.ecl.pokedex.helpers

import com.ecl.pokedex.data.PokemonCardItem
import me.sargunvohra.lib.pokekotlin.model.NamedApiResource

class PokemonListUtils(val namedApiResourceList: List<NamedApiResource>) {

    fun getCardItems(): List<PokemonCardItem> {
        return List(
            namedApiResourceList.size,
            init = {index ->
                namedApiResourceList[index].let { nar ->
                    PokemonCardItem(nar.id, nar.name.replaceFirstChar { it.uppercaseChar() })
                }
            }
        )
    }
}