package com.ecl.pokedex.io

import android.util.Log
import com.ecl.pokedex.data.AppDatabase
import com.ecl.pokedex.data.ECL_Move
import com.ecl.pokedex.data.ECL_Pokemon
import com.ecl.pokedex.interfaces.NetworkReq
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

    fun findMoves(reqs: MutableList<NetworkReq>, callback: (ECL_Move, NetworkReq) -> Unit): MutableList<NetworkReq> {
        val returnReqs: MutableList<NetworkReq> = mutableListOf()
        for (req in reqs) {
            val move = database.movesDao().getMoveById(req.id)
            if (move.isEmpty())
                returnReqs.add(req)
            else
                CoroutineScope(Job()).launch {
                    callback.invoke(move[0], req)
                }
        }
        return returnReqs
    }
}
