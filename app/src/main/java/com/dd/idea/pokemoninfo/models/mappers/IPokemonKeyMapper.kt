package com.dd.idea.pokemoninfo.models.mappers

import com.dd.idea.pokemoninfo.models.Pokemon
import com.dd.idea.pokemoninfo.models.PokemonKey
import com.dd.idea.pokemoninfo.models.responceModels.PokemonPageResponse
import com.dd.idea.pokemoninfo.models.responceModels.PokemonResponse

interface IPokemonKeyMapper {
    fun map(remoteResponse: PokemonPageResponse): PokemonKey
}
