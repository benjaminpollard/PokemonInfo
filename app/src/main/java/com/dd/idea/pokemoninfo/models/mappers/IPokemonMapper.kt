package com.dd.idea.pokemoninfo.models.mappers

import com.dd.idea.pokemoninfo.models.Pokemon
import com.dd.idea.pokemoninfo.models.responceModels.PokemonPageResponse
import com.dd.idea.pokemoninfo.models.responceModels.PokemonResponse

interface IPokemonMapper {
    fun map(remoteResponse: PokemonResponse): Pokemon

    //map remote model to local model we will save and use thought out app
    fun map(remoteResponse: PokemonPageResponse?): List<Pokemon>
}
