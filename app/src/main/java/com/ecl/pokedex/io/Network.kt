package com.ecl.pokedex.io

import com.ecl.pokedex.Globals
import com.ecl.pokedex.data.ECL_Move
import com.ecl.pokedex.data.ECL_Pokemon
import com.ecl.pokedex.data.ECL_PokemonSpecies
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import me.sargunvohra.lib.pokekotlin.model.Generation
import me.sargunvohra.lib.pokekotlin.model.NamedApiResource
import me.sargunvohra.lib.pokekotlin.model.NamedApiResourceList
import me.sargunvohra.lib.pokekotlin.model.Pokedex
import me.sargunvohra.lib.pokekotlin.model.PokemonEntry

class Network {
    private val apiClient = PokeApiClient()
    private val cache = Cache()

    fun getPokemon(id: Int, logRequest: Boolean = true): ECL_Pokemon {
        var pokemon = cache.getPokemon(id, logRequest)
        return if (pokemon != null) {
            pokemon
        } else {
            pokemon = Globals.LocalStorage?.get()?.getPokemon(id)
            if (pokemon != null) {
                cache.addPokemon(pokemon)
                pokemon
            }
            else {
                val poke = ECL_Pokemon(apiClient.getPokemon(id))
                cache.addPokemon(poke)
                poke
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

    fun getPokemonSpecies(id: Int): ECL_PokemonSpecies {
        val pokemonSpecies = cache.getPokemonSpecies(id)
        return if (pokemonSpecies != null) {
            pokemonSpecies
        } else {
            val poke = ECL_PokemonSpecies(apiClient.getPokemonSpecies(id))
            cache.addPokemonSpecies(poke)
            poke
        }
    }

    fun getPokemonSpeciesList(offset: Int, limit: Int): NamedApiResourceList {
        return apiClient.getPokemonSpeciesList(offset, limit)
    }

    fun getPokedex(id: Int): Pokedex {
        val pokedex = cache.getPokedex(id)
        return if (pokedex != null) {
            pokedex
        } else {
            val dex = apiClient.getPokedex(id)
            cache.addPokedex(dex)
            dex
        }
    }

    fun getPokedexEntryList(dexId: Int, offset: Int, limit: Int): List<PokemonEntry> {
        val entries = getPokedex(dexId).pokemonEntries
        return if (offset < entries.lastIndex)
            if (limit > entries.count())
                entries.subList(offset, entries.lastIndex)
            else
                entries.subList(offset, limit)
        else
            listOf()
    }

    fun getGendex(id: Int): Generation {
        val generation = cache.getGeneration(id)
        return if (generation != null) {
            generation
        } else {
            val gen = apiClient.getGeneration(id)
            cache.addGeneration(gen)
            gen
        }
    }

    fun getGenerations(): List<NamedApiResource> {
        return apiClient.getGenerationList(0, 10000).results
    }

    fun getMoveData(id: Int, logRequest: Boolean = true): ECL_Move {
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
                move = ECL_Move(apiClient.getMove(id))
                cache.addMove(move)
                move
            }
        }
    }

    fun onPause() {
        cache.clear()
    }

    fun addCacheListener(cacheListener: CacheListener) = cache.addCacheListener(cacheListener)
    fun removeCacheListener(cacheListener: CacheListener) = cache.removeCacheListener(cacheListener)
}