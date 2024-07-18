package com.ecl.pokedex.helpers

import com.ecl.pokedex.data.ECL_NAPI_Resource
import com.ecl.pokedex.data.PokemonCardItem

class PokemonListUtils(val namedApiResourceList: List<ECL_NAPI_Resource>) {

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

    fun getPokemonNames(): Array<String> {
        return Array(namedApiResourceList.size) {
            namedApiResourceList[it].name.replaceFirstChar { it.uppercaseChar() }
        }
    }
}