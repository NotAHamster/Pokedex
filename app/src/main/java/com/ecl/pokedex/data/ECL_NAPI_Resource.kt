package com.ecl.pokedex.data

import me.sargunvohra.lib.pokekotlin.model.NamedApiResource

data class ECL_NAPI_Resource(val id: Int, val name: String) {

    constructor(namedApiResource: NamedApiResource): this(
        namedApiResource.id,
        namedApiResource.name
    )
}