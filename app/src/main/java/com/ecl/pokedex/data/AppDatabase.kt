package com.ecl.pokedex.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ecl.pokedex.data.converters.PokemonMovesConverter
import com.ecl.pokedex.data.converters.PokemonStatsConverter
import com.ecl.pokedex.data.converters.SpeciesVarietiesConverter
import com.ecl.pokedex.data.converters.SpritesConverter
import com.ecl.pokedex.data.dao.ECL_Move_Dao
import com.ecl.pokedex.data.dao.ECL_Pokemon_Dao

@Database(entities = [ECL_Pokemon::class, ECL_PokemonSpecies::class, ECL_Move::class], version = 1)
@TypeConverters(
    SpritesConverter::class,
    PokemonMovesConverter::class,
    PokemonStatsConverter::class,
    SpeciesVarietiesConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): ECL_Pokemon_Dao
    abstract fun movesDao(): ECL_Move_Dao
}