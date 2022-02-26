package com.dd.idea.pokemoninfo.models.responceModels

import com.google.gson.annotations.SerializedName

data class PokemonDetailsResponse(
        @SerializedName("id") var id: Int,
        @SerializedName("name") var name: String,
        @SerializedName("sprites") var sprit: ImageHolder,
        @SerializedName("types") var types: List<TypeHolder>,
        @SerializedName("weight") var weight: String,
        @SerializedName("height") var height: String
)

data class ImageHolder(@SerializedName("front_default") val artwork: String)
data class TypeHolder(@SerializedName("type") val type: Type)
data class Type(@SerializedName("name") val name: String)