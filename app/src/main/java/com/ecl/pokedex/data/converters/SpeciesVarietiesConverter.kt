package com.ecl.pokedex.data.converters

import androidx.room.TypeConverter
import com.ecl.pokedex.data.Variety
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SpeciesVarietiesConverter {
    @TypeConverter
    fun fromString(value: String): List<Variety> {
        val type = object : TypeToken<List<Variety>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromMoves(moves: List<Variety>): String {
        return Gson().toJson(moves)
    }
}