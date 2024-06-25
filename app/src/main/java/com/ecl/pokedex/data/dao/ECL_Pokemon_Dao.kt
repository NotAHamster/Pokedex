package com.ecl.pokedex.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import com.ecl.pokedex.data.ECL_Pokemon
import com.ecl.pokedex.data.converters.PokemonMovesConverter
import com.ecl.pokedex.data.converters.PokemonStatsConverter
import com.ecl.pokedex.data.converters.SpritesConverter

@TypeConverters(SpritesConverter::class, PokemonMovesConverter::class, PokemonStatsConverter::class)
@Dao
interface ECL_Pokemon_Dao {
    @Insert
    fun insert(pokemon: ECL_Pokemon)

    @Query("SELECT * FROM pokemon WHERE id = :id")
    fun getPokemonById(id: Int): List<ECL_Pokemon>

    @Query("SELECT id From pokemon")
    fun getPokemonIds(): List<Int>
}