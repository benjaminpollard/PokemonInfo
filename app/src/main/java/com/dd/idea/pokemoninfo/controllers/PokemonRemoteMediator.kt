package com.dd.idea.pokemoninfo.controllers

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.dd.idea.pokemoninfo.models.Pokemon
import com.dd.idea.pokemoninfo.models.PokemonKey
import com.dd.idea.pokemoninfo.models.mappers.IPokemonKeyMapper
import com.dd.idea.pokemoninfo.models.mappers.IPokemonMapper
import com.dd.idea.pokemoninfo.services.DatabaseService
import com.dd.idea.pokemoninfo.services.IBaseNetworkService
import com.dd.idea.pokemoninfo.services.PokemonService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class PokemonRemoteMediator(
    private val service: IBaseNetworkService,
    private val pokemonMapper: IPokemonMapper,
    private val pokemonKeyMapper: IPokemonKeyMapper,
    private val database: DatabaseService
) : RemoteMediator<Int, Pokemon>() {

    private fun getPokemonService() =
        service.serviceConstructor(PokemonService::class.java) as PokemonService

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Pokemon>
    ): MediatorResult {
        return try {
            //on fresh, go back to the start, on PREPEND we don't need to worry about as we start at the start of the list
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    getKey()
                }
            }

            val response = if (loadKey == null) {
                database.clearAllTables()
                getPokemonService().getList(0, state.config.pageSize)
            } else {
                loadKey.next?.let {
                    getPokemonService().getList(it)
                }
            }

            response?.let {
                val keyToSave = pokemonKeyMapper.map(it)
                val items = pokemonMapper.map(it)

                CoroutineScope(Dispatchers.IO).launch {
                    database.pokemonDao().insertAll(items)
                    database.pokemonKeyDao().saveKeys(keyToSave)
                }
            }

            MediatorResult.Success(endOfPaginationReached = response?.next == null)

        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getKey(): PokemonKey? {
        return database.pokemonKeyDao().getKeys().lastOrNull()
    }
}