package com.dd.idea.pokemoninfo.models.mappers

import com.dd.idea.pokemoninfo.models.PokemonDetails
import com.dd.idea.pokemoninfo.models.responceModels.PokemonDetailsResponse
import java.util.*

class PokemonDetailsMapper {
    //map remote model to local model we will save and use thought out app
    fun map(remoteResponse: PokemonDetailsResponse): PokemonDetails {
        return PokemonDetails().apply {
            height = remoteResponse.height
            image = remoteResponse.sprit.artwork
            weight = remoteResponse.weight
            name = remoteResponse.name

            for (rt in remoteResponse.types) {
                type = if (type.isNotBlank())
                    "$type , ${rt.type.name.capitalize(Locale.ROOT)}"
                else
                    rt.type.name.capitalize(Locale.ROOT)
            }
        }
    }

}