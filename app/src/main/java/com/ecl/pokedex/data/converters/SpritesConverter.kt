package com.ecl.pokedex.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.sargunvohra.lib.pokekotlin.model.PokemonSprites

class SpritesConverter {
    @TypeConverter
    fun fromString(value: String): PokemonSprites {
        val type = object : TypeToken<PokemonSprites>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromSprites(sprites: PokemonSprites): String {
        return Gson().toJson(sprites)
    }
}