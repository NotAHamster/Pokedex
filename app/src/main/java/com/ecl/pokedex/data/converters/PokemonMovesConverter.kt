package com.ecl.pokedex.data.converters

import androidx.room.TypeConverter
import com.ecl.pokedex.data.ECL_PokemonMove
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PokemonMovesConverter {
    @TypeConverter
    fun fromString(value: String): List<ECL_PokemonMove> {
        val type = object : TypeToken<List<ECL_PokemonMove>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromMoves(moves: List<ECL_PokemonMove>): String {
        return Gson().toJson(moves)
    }
}