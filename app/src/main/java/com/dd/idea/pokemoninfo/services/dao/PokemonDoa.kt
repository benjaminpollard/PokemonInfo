package com.dd.idea.pokemoninfo.services.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.dd.idea.pokemoninfo.models.Pokemon

@Dao
interface PokemonDoa {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<Pokemon>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Pokemon)

    @Query("SELECT * FROM pokemon")
    fun getAll(): PagingSource<Int, Pokemon>

    @Delete
    fun delete(user: Pokemon)
}