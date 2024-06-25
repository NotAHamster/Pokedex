package com.ecl.pokedex.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.sargunvohra.lib.pokekotlin.model.Move

@Entity(tableName = "moves")
class ECL_Move(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String,
    val power: Int?,
    val acc: Int?,
    val pp: Int?
) {

    constructor(move: Move) : this(
        move.id,
        move.name,
        move.type.name,
        move.power,
        move.accuracy,
        move.pp
    )
}