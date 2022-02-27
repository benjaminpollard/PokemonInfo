package com.dd.idea.pokemoninfo.models

import io.realm.annotations.PrimaryKey

open class Pokemon {
    @PrimaryKey
    var name: String = ""
    var url: String = ""
}