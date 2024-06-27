package com.ecl.pokedex.io

import android.util.Log
import com.ecl.pokedex.data.AppDatabase
import com.ecl.pokedex.data.ECL_Move
import com.ecl.pokedex.data.ECL_Pokemon

class LocalStorage(private val database: AppDatabase) {
    private val pokemonIds: MutableList<Int>
    private val moveIds: MutableList<Int>

    init {
        pokemonIds = database.pokemonDao().getPokemonIds().toMutableList()
        moveIds = database.movesDao().getMoveIds().toMutableList()
        Log.d("ecl.LocalStorage", "local storage loaded! ^.^")
    }

    fun addPokemon(pokemon: ECL_Pokemon) {
        synchronized(this) {
            if (!pokemonIds.contains(pokemon.id)) {
                pokemonIds.add(pokemon.id)
                database.pokemonDao().insert(pokemon)
                Log.d("ecl.LocalStorage", "${pokemon.name} added to database")
            }
        }
    }

    fun getPokemon(id: Int): ECL_Pokemon? {
        val res = database.pokemonDao().getPokemonById(id)
        Log.d("ecl.LocalStorage", "request in pokemon for $id")
        return if (res.isNotEmpty())
            res[0]
        else
            null
    }

    fun getMove(id: Int): ECL_Move? {
        val res = database.movesDao().getMoveById(id)
        Log.d("ecl.LocalStorage", "request in moves for $id")
        return if (res.isNotEmpty())
            res[0]
        else
            null
    }

    fun addMove(move: ECL_Move) {
        synchronized(this) {
            if (!moveIds.contains(move.id)) {
                moveIds.add(move.id)
                database.movesDao().insert(move)
                Log.d("ecl.LocalStorage", "${move.name} added to database")
            }
        }
    }
}
