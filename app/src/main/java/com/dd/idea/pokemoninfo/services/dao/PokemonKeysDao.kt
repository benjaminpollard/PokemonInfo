package com.dd.idea.pokemoninfo.services.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.dd.idea.pokemoninfo.models.PokemonKey

@Dao
interface PokemonKeysDao {
    @Insert(onConflict = REPLACE)
    suspend fun saveKeys(key: PokemonKey)

    @Query("SELECT * FROM pokemonKey")
    suspend fun getKeys(): List<PokemonKey>
}