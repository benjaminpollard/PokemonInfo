package com.dd.idea.pokemoninfo.services


import com.dd.idea.pokemoninfo.models.responceModels.PokemonDetailsResponse
import com.dd.idea.pokemoninfo.models.responceModels.PokemonPageResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface PokemonService {

    companion object {
        const val OFF_SET = "offset"
        const val LIMIT = "limit"
    }

    @GET("pokemon")
    suspend fun getList(@Query(OFF_SET) offset: Int, @Query(LIMIT) limit: Int): PokemonPageResponse

    @GET
    suspend fun getList(@Url url: String): PokemonPageResponse

    @GET
    suspend fun getPokemonDetails(@Url url: String): PokemonDetailsResponse
}