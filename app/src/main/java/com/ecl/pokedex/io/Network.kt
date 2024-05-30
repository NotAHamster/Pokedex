package com.ecl.pokedex.io

import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import me.sargunvohra.lib.pokekotlin.model.Generation
import me.sargunvohra.lib.pokekotlin.model.Move
import me.sargunvohra.lib.pokekotlin.model.NamedApiResource
import me.sargunvohra.lib.pokekotlin.model.NamedApiResourceList
import me.sargunvohra.lib.pokekotlin.model.Pokedex
import me.sargunvohra.lib.pokekotlin.model.Pokemon
import me.sargunvohra.lib.pokekotlin.model.PokemonEntry
import me.sargunvohra.lib.pokekotlin.model.PokemonSpecies

class Network {
    private val apiClient = PokeApiClient()
    private val cache = Cache()

    private class Cache {
        private val pokemon: MutableList<Pokemon> = mutableListOf()
        private val pokemonSpecies: MutableList<PokemonSpecies> = mutableListOf()
        private val pokedex: MutableList<Pokedex> = mutableListOf()
        private val generation: MutableList<Generation> = mutableListOf()

        fun getPokemon(id: Int): Pokemon? {
            return pokemon.find { it.id == id }
        }

        fun getPokemon(name: String): Pokemon? {
            return pokemon.find { it.name == name }
        }

        fun addPokemon(pokemon: Pokemon) {
            this.pokemon.add(pokemon)
        }

        fun getPokemonSpecies(id: Int): PokemonSpecies? {
            return pokemonSpecies.find { it.id == id }
        }

        fun addPokemonSpecies(pokemonSpecies: PokemonSpecies) {
            this.pokemonSpecies.add(pokemonSpecies)
        }

        fun getPokedex(id: Int): Pokedex? {
            return pokedex.find { it.id == id }
        }

        fun addPokedex(pokedex: Pokedex) {
            this.pokedex.add(pokedex)
        }

        fun getGeneration(id: Int): Generation? {
            return generation.find { it.id == id }
        }

        fun addGeneration(generation: Generation) {
            this.generation.add(generation)
        }

        fun clear() {
            pokemon.clear()
            pokemonSpecies.clear()
        }
    }

    fun getPokemon(id: Int): Pokemon {
        val pokemon = cache.getPokemon(id)
        return if (pokemon != null) {
            pokemon
        } else {
            val poke = apiClient.getPokemon(id)
            cache.addPokemon(poke)
            poke
        }
    }

    /**
     * Retrieves a pokemon from the network cache.
     *
     * This does not try to request information from the API use if expecting the result to be cached.
     */
    fun getPokemon(name: String): Pokemon? {
        return cache.getPokemon(name.lowercase())
    }

    fun getPokemonSpecies(id: Int): PokemonSpecies {
        val pokemonSpecies = cache.getPokemonSpecies(id)
        return if (pokemonSpecies != null) {
            pokemonSpecies
        } else {
            val poke = apiClient.getPokemonSpecies(id)
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

    fun getMoveData(id: Int): Move {
        return apiClient.getMove(id)
    }

    fun onPause() {
        cache.clear()
    }
}