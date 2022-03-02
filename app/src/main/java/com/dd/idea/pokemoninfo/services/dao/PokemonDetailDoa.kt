package com.dd.idea.pokemoninfo.services.dao

import androidx.room.*
import com.dd.idea.pokemoninfo.models.Pokemon
import com.dd.idea.pokemoninfo.models.PokemonDetails

@Dao
interface PokemonDetailDoa {
    @Query("SELECT * FROM pokemondetails")
    fun getAll(): List<PokemonDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<PokemonDetails>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: PokemonDetails)

    @Delete
    fun delete(user: PokemonDetails)
}