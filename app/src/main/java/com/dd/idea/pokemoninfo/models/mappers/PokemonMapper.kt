package com.dd.idea.pokemoninfo.models.mappers

import com.dd.idea.pokemoninfo.models.Pokemon
import com.dd.idea.pokemoninfo.models.responceModels.PokemonPageResponse
import com.dd.idea.pokemoninfo.models.responceModels.PokemonResponse

class PokemonMapper : IPokemonMapper {
    override fun map(remoteResponse: PokemonResponse): Pokemon {
        return Pokemon(remoteResponse.name , remoteResponse.url)
    }

    //map remote model to local model we will save and use thought out app
    override fun map(remoteResponse: PokemonPageResponse?): List<Pokemon> {
        val items = mutableListOf<Pokemon>()
        remoteResponse?.results?.forEach {
            items.add(map(it))
        }
        return items
    }
}