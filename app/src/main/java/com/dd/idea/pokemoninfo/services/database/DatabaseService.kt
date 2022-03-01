package com.dd.idea.pokemoninfo.services.database

import io.realm.Realm
import io.realm.RealmObject
import io.realm.kotlin.executeTransactionAwait
import io.realm.kotlin.toFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class DatabaseService : IDatabaseService {

    private var realm: Realm = Realm.getDefaultInstance()

    override suspend fun <T : RealmObject> updateOrInsertItems(item: List<RealmObject>) {
        realm.executeTransactionAwait(Dispatchers.IO) {
            it.insertOrUpdate(item)
        }
    }

    override suspend fun <T : RealmObject> updateOrInsertItem(item: T) {
        realm.executeTransactionAwait(Dispatchers.IO) {
            it.insertOrUpdate(item)
        }
    }

    override suspend fun update(update: DatabaseUpdate) {
        realm.executeTransactionAwait(Dispatchers.IO) {
            it.beginTransaction()
            update.update()
            it.commitTransaction()
        }
    }

    override fun <T : RealmObject> getItem(type: Class<T>): Flow<T?> {
        val query = realm.where(type)
        return query.findFirstAsync().toFlow()
    }

    override fun <T : RealmObject> getItems(type: Class<T>): Flow<List<T>?> {
        val query = realm.where(type)
        return query.findAllAsync().toFlow()
    }

    override suspend fun <T : RealmObject> saveItem(item: T) {
        realm.executeTransactionAwait(Dispatchers.IO) {
            it.copyToRealm(item)
        }
    }

    override suspend fun <T : RealmObject> saveItems(items: List<T>) {
        realm.executeTransactionAwait(Dispatchers.IO) {
            for (item in items) {
                it.copyToRealm(item)
            }
        }
    }

    override suspend fun <T : RealmObject> remove(item: Class<T>) {
        realm.executeTransactionAwait(Dispatchers.IO) {
            it.delete(item)
        }
    }

    override fun dropDatabase() {
        realm.executeTransactionAsync {
            realm.deleteAll()
        }
    }

}
