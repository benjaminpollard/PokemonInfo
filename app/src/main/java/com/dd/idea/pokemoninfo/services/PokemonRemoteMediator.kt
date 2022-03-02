package com.dd.idea.pokemoninfo.services

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.dd.idea.pokemoninfo.models.Pokemon
import com.dd.idea.pokemoninfo.models.PokemonKey
import com.dd.idea.pokemoninfo.models.mappers.IPokemonKeyMapper
import com.dd.idea.pokemoninfo.models.mappers.IPokemonMapper
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
        // 1
        loadType: LoadType,
        // 2
        state: PagingState<Int, Pokemon>
    ): MediatorResult {
        return try {

            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    getKey()
                }
            }

            val response = if(loadKey?.previous == null) {
                getPokemonService().getList(
                    0 ,
                    state.config.pageSize
                )
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

    private suspend fun getKey(): PokemonKey {
        return database.pokemonKeyDao().getKeys().first()
    }
}