package com.ecl.pokedex.io

import com.ecl.pokedex.data.ECL_Move
import com.ecl.pokedex.data.ECL_Pokemon
import com.ecl.pokedex.data.ECL_PokemonSpecies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.sargunvohra.lib.pokekotlin.model.Generation
import me.sargunvohra.lib.pokekotlin.model.Pokedex

class Cache {
    private val pokemon: MutableList<ECL_Pokemon> = mutableListOf()
    private val pokemonSpecies: MutableList<ECL_PokemonSpecies> = mutableListOf()
    private val pokedex: MutableList<Pokedex> = mutableListOf()
    private val generation: MutableList<Generation> = mutableListOf()
    private val moves: MutableList<ECL_Move> = mutableListOf()

    private val cacheListeners: MutableList<CacheListener> = mutableListOf()

    fun getPokemon(id: Int, logRequest: Boolean): ECL_Pokemon? {
        if (logRequest)
            CoroutineScope(Dispatchers.IO).launch {
                cacheListeners.forEach {
                    it.onGetPokemonById(id)
                }
            }
        //return pokemon.find { it.id == id }

        return synchronized(pokemon) {
            pokemon.find { it.id == id }
        }
    }

    fun getPokemon(name: String): ECL_Pokemon? {
        cacheListeners.forEach { it.onGetPokemonByName(name) }
        return pokemon.find { it.name == name }
    }

    fun addPokemon(pokemon: ECL_Pokemon) {
        this.pokemon.add(pokemon)
    }

    fun getPokemonSpecies(id: Int): ECL_PokemonSpecies? {
        cacheListeners.forEach { it.onGetPokemonSpecies(id) }
        return pokemonSpecies.find { it.id == id }
    }

    fun addPokemonSpecies(pokemonSpecies: ECL_PokemonSpecies) {
        this.pokemonSpecies.add(pokemonSpecies)
    }

    fun getPokedex(id: Int): Pokedex? {
        cacheListeners.forEach { it.onGetPokedex(id) }
        return pokedex.find { it.id == id }
    }

    fun addPokedex(pokedex: Pokedex) {
        this.pokedex.add(pokedex)
    }

    fun getGeneration(id: Int): Generation? {
        cacheListeners.forEach { it.onGetGeneration(id) }
        return generation.find { it.id == id }
    }

    fun addGeneration(generation: Generation) {
        this.generation.add(generation)
    }

    fun getMove(id: Int, logRequest: Boolean): ECL_Move? {
        if (logRequest)
            CoroutineScope(Dispatchers.IO).launch {
                cacheListeners.forEach {
                    it.onGetMove(id)
                }
            }
        //return moves.find { it.id == id }

        return synchronized(moves) {
            moves.find { it.id == id }
        }
    }

    fun addMove(move: ECL_Move) {
        this.moves.add(move)
    }

    fun clear() {
        pokemon.clear()
        pokemonSpecies.clear()
        moves.clear()
    }

    fun addCacheListener(cacheListener: CacheListener) {
        if (!cacheListeners.contains(cacheListener))
            cacheListeners.add(cacheListener)
    }
    fun removeCacheListener(cacheListener: CacheListener) {
        cacheListeners.remove(cacheListener)
    }
}