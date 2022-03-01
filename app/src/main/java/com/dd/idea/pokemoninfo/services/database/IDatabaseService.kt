package com.dd.idea.pokemoninfo.services.database

import io.realm.RealmObject
import kotlinx.coroutines.flow.Flow

interface IDatabaseService {
    suspend fun updateOrInsertItems(item: List<RealmObject>)
    suspend fun <T : RealmObject> updateOrInsertItem(item: T)
    fun <T : RealmObject> getItem(type: Class<T>): Flow<T?>
    fun <T : RealmObject> getItems(type: Class<T>): Flow<List<T>?>
    suspend fun <T : RealmObject> saveItem(item: T)
    suspend fun <T : RealmObject> saveItems(items: List<T>)
    suspend fun update(update: DatabaseUpdate)
    suspend fun <T : RealmObject> remove(item: Class<T>)
    fun dropDatabase()
}