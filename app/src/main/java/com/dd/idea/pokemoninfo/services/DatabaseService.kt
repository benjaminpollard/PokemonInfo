package com.dd.idea.pokemoninfo.services

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dd.idea.pokemoninfo.models.Pokemon
import com.dd.idea.pokemoninfo.models.PokemonDetails
import com.dd.idea.pokemoninfo.models.PokemonKey
import com.dd.idea.pokemoninfo.services.dao.PokemonDetailDoa
import com.dd.idea.pokemoninfo.services.dao.PokemonDoa
import com.dd.idea.pokemoninfo.services.dao.PokemonKeysDao

@Database(entities = [Pokemon::class,PokemonDetails::class, PokemonKey::class], version = 1)
abstract class DatabaseService : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDoa
    abstract fun pokemonDetailDao(): PokemonDetailDoa
    abstract fun pokemonKeyDao(): PokemonKeysDao
}