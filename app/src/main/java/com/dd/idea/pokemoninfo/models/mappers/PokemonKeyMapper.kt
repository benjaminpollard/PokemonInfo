package com.dd.idea.pokemoninfo.models.mappers

import com.dd.idea.pokemoninfo.models.PokemonKey
import com.dd.idea.pokemoninfo.models.responceModels.PokemonPageResponse

class PokemonKeyMapper : IPokemonKeyMapper {

    //map remote model to local model we will save and use thought out app
    override fun map(remoteResponse: PokemonPageResponse) =
        PokemonKey(remoteResponse.count, remoteResponse.next , remoteResponse.previous)

}