package com.dd.idea.pokemoninfo.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonKey(
    val total: Long,
    val next: String?,
    val previous: String?,
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
)