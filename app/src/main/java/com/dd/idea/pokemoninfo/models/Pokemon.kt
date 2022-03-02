package com.dd.idea.pokemoninfo.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pokemon  (
    @PrimaryKey
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "url") val url: String
)