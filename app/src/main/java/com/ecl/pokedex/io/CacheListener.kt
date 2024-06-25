package com.ecl.pokedex.io

import com.ecl.pokedex.Globals.network

class CacheListener(val localStorage: LocalStorage) {
    private val threshold: Float = 2f

    private val pokemonIds: MutableMap<Int, Int> = mutableMapOf()
    private var pokemonIdsSum: Float = pokemonIds.values.sum().toFloat()
    suspend fun onGetPokemonById(id: Int) {
        var sumOfId = 0
        val count = synchronized(pokemonIds) {
            sumOfId = pokemonIds.compute(id) { _, value -> (value ?: 0) + 1 }!!
            pokemonIdsSum++
            pokemonIds.count()
        }
        if (sumOfId > (pokemonIdsSum / count) + threshold) {
            localStorage.addPokemon(network.getPokemon(id, logRequest = false))
        }
    }

    //private val pokemonNames: MutableMap<String, Int> = mutableMapOf()
    //private var pokemonNamesSum: Float = pokemonNames.values.sum().toFloat()
    fun onGetPokemonByName(name: String) {
        //do nothing
    }

    private val pokemonSpecies: MutableMap<Int, Int> = mutableMapOf()
    private var pokemonSpeciesSum: Float = pokemonSpecies.values.sum().toFloat()
    fun onGetPokemonSpecies(id: Int) {
        val count = pokemonSpecies.compute(id) { _, value -> (value ?: 0) + 1 }!!
        pokemonSpeciesSum++
        if (count > (pokemonSpeciesSum / pokemonSpecies.count()) + threshold) {
            //TODO("Save the value locally for faster access in the future")
        }
    }

    private val pokedex: MutableMap<Int, Int> = mutableMapOf()
    private var pokedexSum: Float = pokedex.values.sum().toFloat()
    fun onGetPokedex(id: Int) {
        val count = pokedex.compute(id) { _, value -> (value ?: 0) + 1 }!!
        pokedexSum++
        if (count > (pokedexSum / pokedex.count()) + threshold) {
            //TODO("Save the value locally for faster access in the future")
        }
    }

    private val generation: MutableMap<Int, Int> = mutableMapOf()
    private var generationSum: Float = pokedex.values.sum().toFloat()
    fun onGetGeneration(id: Int) {
        val count = generation.compute(id) { _, value -> (value ?: 0) + 1 }!!
        generationSum++
        if (count > (generationSum / generation.count()) + threshold) {
            //TODO("Save the value locally for faster access in the future")
        }
    }

    private val moves: MutableMap<Int, Int> = mutableMapOf()
    private var movesSum: Float = moves.values.sum().toFloat()
    fun onGetMove(id: Int) {
        val count = moves.compute(id) { _, value -> (value ?: 0) + 1}!!
        movesSum++
        if (count > (movesSum / moves.count()) + threshold) {
            localStorage.addMove(network.getMoveData(id, logRequest = false))
        }
    }
}