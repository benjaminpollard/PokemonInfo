package com.dd.idea.pokemoninfo.models.mappers

import com.dd.idea.pokemoninfo.models.PokemonDetails
import com.dd.idea.pokemoninfo.models.responceModels.PokemonDetailsResponse

interface IPokemonDetailsMapper {
    //map remote model to local model we will save and use thought out app
    fun map(remoteResponse: PokemonDetailsResponse): PokemonDetails
}