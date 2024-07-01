package com.ecl.pokedex.data

import com.ecl.pokedex.adapters.ELV_NavAdapter

data class VerItemData(
    override val name: String,
    val id: Int,
    var active: Boolean = false
): ELV_NavAdapter.Child()