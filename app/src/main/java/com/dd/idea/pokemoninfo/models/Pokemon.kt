package com.dd.idea.pokemoninfo.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Pokemon : RealmObject() {
    @PrimaryKey
    var name: String = ""
    var url: String = ""
}