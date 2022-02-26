package com.dd.idea.pokemoninfo.models.responceModels

data class PokemonPageResponse(
    val count: Long,
    val next: String?,
    val previous: String? = null,
    val results: ArrayList<PokemonResponse>
)