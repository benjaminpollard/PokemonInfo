package com.dd.idea.pokemoninfo.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonDetails(
    @PrimaryKey val uid: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "weight") val weight: String,
    @ColumnInfo(name = "height") val height: String,
)