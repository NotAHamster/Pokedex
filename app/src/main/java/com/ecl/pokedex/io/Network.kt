package com.ecl.pokedex.io

import android.util.Log
import com.ecl.pokedex.Globals
import com.ecl.pokedex.data.ECL_Generation
import com.ecl.pokedex.data.ECL_Move
import com.ecl.pokedex.data.ECL_NAPI_Resource
import com.ecl.pokedex.data.ECL_Pokemon
import com.ecl.pokedex.data.ECL_PokemonSpecies
import com.ecl.pokedex.interfaces.NetworkReq
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import me.sargunvohra.lib.pokekotlin.model.Pokedex
import java.net.UnknownHostException

class Network {
    private val apiClient = PokeApiClient()
    private val cache = Cache()

    fun getPokemon(id: Int, logRequest: Boolean = true): ECL_Pokemon? {
        var pokemon = cache.getPokemon(id, logRequest)
        if (pokemon != null) {
            return pokemon
        } else {
            pokemon = Globals.LocalStorage?.get()?.getPokemon(id)
            if (pokemon != null) {
                cache.addPokemon(pokemon)
                return pokemon
            }
            else {
                try {
                    val poke = ECL_Pokemon(apiClient.getPokemon(id))
                    cache.addPokemon(poke)
                    return poke
                } catch (e: UnknownHostException) {
                    Log.e("com.ecl.network", "$e")
                }
                return null
            }
        }
    }

    /**
     * Retrieves a pokemon from the network cache.
     *
     * This does not try to request information from the API use if expecting the result to be cached.
     */
    fun getPokemon(name: String): ECL_Pokemon? {
        return cache.getPokemon(name)
    }

    fun getPokemonSpecies(id: Int): ECL_PokemonSpecies? {
        var pokemonSpecies = cache.getPokemonSpecies(id)
        if (pokemonSpecies != null) {
            return pokemonSpecies
        } else {
            try {
                pokemonSpecies = ECL_PokemonSpecies(apiClient.getPokemonSpecies(id))
                cache.addPokemonSpecies(pokemonSpecies)
                return pokemonSpecies
            } catch (e: UnknownHostException) {
                Log.e("com.ecl.network", "$e")
            }
            return null
        }
    }

    fun getPokemonSpeciesList(offset: Int, limit: Int): List<ECL_NAPI_Resource> {
        try {
            val res = apiClient.getPokemonSpeciesList(offset, limit).results
            return List(res.size) {
                ECL_NAPI_Resource(res[it])
            }
        } catch (e: UnknownHostException) {
            Log.e("com.ecl.network", "$e")
        }
        return listOf()
    }

    fun getPokedex(id: Int): Pokedex? {
        var pokedex = cache.getPokedex(id)
        if (pokedex != null) {
            return pokedex
        } else {
            try {
                pokedex = apiClient.getPokedex(id)
                cache.addPokedex(pokedex)
                return pokedex
            } catch (e: UnknownHostException) {
                Log.e("com.ecl.network", "$e")
            }
            return null
        }
    }

    /*fun getPokedexEntryList(dexId: Int, offset: Int, limit: Int): List<PokemonEntry>? {
        val entries = getPokedex(dexId).pokemonEntries
        return if (offset < entries.lastIndex)
            if (limit > entries.count())
                entries.subList(offset, entries.lastIndex)
            else
                entries.subList(offset, limit)
        else
            listOf()
    }*/

    fun getGendex(id: Int): ECL_Generation? {
        var generation = cache.getGeneration(id)
        if (generation != null) {
            return generation
        } else {
            try {
                generation = ECL_Generation(apiClient.getGeneration(id))
                cache.addGeneration(generation)
                return generation
            } catch (e: UnknownHostException) {
                Log.e("com.ecl.network", "$e")
            }
            return null
        }
    }

    fun getGenerations(): List<ECL_NAPI_Resource> {
        try {
            val res = apiClient.getGenerationList(0, 10000).results
            return List(res.size) {
                ECL_NAPI_Resource(res[it])
            }
        } catch (e: UnknownHostException) {
            Log.e("com.ecl.network", "$e")
        }
        return listOf()
    }

    fun getMoveData(id: Int, logRequest: Boolean = true): ECL_Move? {
        var move = cache.getMove(id, logRequest)
        return if (move != null) {
            move
        } else {
            move = Globals.LocalStorage?.get()?.getMove(id)
            if (move != null) {
                cache.addMove(move)
                move
            }
            else {
                try {
                    move = ECL_Move(apiClient.getMove(id))
                    cache.addMove(move)
                    return move
                } catch (e: UnknownHostException) {
                    Log.e("com.ecl.network", "$e")
                }
                return null
            }
        }
    }

    fun getMoveData(reqs: MutableList<NetworkReq>, callback: (ECL_Move, NetworkReq) -> Unit) {
        var reqs = cache.findMoves(reqs, callback)
        if (reqs.isEmpty()) return
        reqs = Globals.LocalStorage?.get()?.findMoves(reqs, callback)?: reqs
        for (req in reqs) {
            try {
                val move = ECL_Move(apiClient.getMove(req.id))
                cache.addMove(move)
                CoroutineScope(Job()).launch {
                    callback.invoke(move, req)
                }
            } catch (e: UnknownHostException) {
                Log.e("com.ecl.network", "$e")
                return
            }
        }
    }

    fun getVersions(): List<ECL_NAPI_Resource> {
        try {
            val res = apiClient.getVersionGroupList(0, 10000).results
            return List(res.size) {
                ECL_NAPI_Resource(res[it])
            }
        } catch (e: UnknownHostException) {
            Log.e("com.ecl.network", "$e")
        }
        return listOf()
    }

    fun onPause() {
        cache.clear()
    }

    fun addCacheListener(cacheListener: CacheListener) = cache.addCacheListener(cacheListener)
    fun removeCacheListener(cacheListener: CacheListener) = cache.removeCacheListener(cacheListener)
}