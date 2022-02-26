package com.dd.idea.pokemoninfo.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class PokemonDetails : RealmObject() {
    @PrimaryKey
    var name = ""
    var image = ""
    var type = ""
    var weight = ""
    var height = ""
}