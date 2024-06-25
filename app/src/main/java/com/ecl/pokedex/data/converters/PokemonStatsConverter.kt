package com.ecl.pokedex.data.converters

import androidx.room.TypeConverter
import com.ecl.pokedex.data.ECL_PokemonStat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PokemonStatsConverter {
    @TypeConverter
    fun fromString(value: String): List<ECL_PokemonStat> {
        val type = object : TypeToken<List<ECL_PokemonStat>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromStats(moves: List<ECL_PokemonStat>): String {
        return Gson().toJson(moves)
    }
}