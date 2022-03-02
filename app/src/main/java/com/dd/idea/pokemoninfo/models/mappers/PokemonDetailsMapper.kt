package com.dd.idea.pokemoninfo.models.mappers

import com.dd.idea.pokemoninfo.models.PokemonDetails
import com.dd.idea.pokemoninfo.models.responceModels.PokemonDetailsResponse
import java.util.*

class PokemonDetailsMapper : IPokemonDetailsMapper {

    //map remote model to local model we will save and use thought out app
    override fun map(remoteResponse: PokemonDetailsResponse): PokemonDetails {

        var type = ""

        for (rt in remoteResponse.types) {
            type = if (type.isNotBlank())
                "$type , ${rt.type.name.capitalize(Locale.ROOT)}"
            else
                rt.type.name.capitalize(Locale.ROOT)
        }

        return PokemonDetails(remoteResponse.id ,
            remoteResponse.name,
            remoteResponse.sprit.artwork,
            type,
            remoteResponse.weight,
            remoteResponse.height
        )
    }

}