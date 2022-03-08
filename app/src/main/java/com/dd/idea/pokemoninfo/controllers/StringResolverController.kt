package com.dd.idea.pokemoninfo.controllers

import android.content.Context

class StringResolverController(private val context: Context) {

    fun get(id : Int) : String {
        return context.getString(id)
    }

}