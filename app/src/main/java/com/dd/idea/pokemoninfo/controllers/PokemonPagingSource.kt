package com.dd.idea.pokemoninfo.controllers

import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.dd.idea.pokemoninfo.models.Pokemon
import com.dd.idea.pokemoninfo.models.mappers.IPokemonKeyMapper
import com.dd.idea.pokemoninfo.models.mappers.IPokemonMapper
import com.dd.idea.pokemoninfo.services.DatabaseService
import com.dd.idea.pokemoninfo.services.IBaseNetworkService
import com.dd.idea.pokemoninfo.services.PokemonService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class PokemonPagingSource(
    private val networkService: IBaseNetworkService,
    private val databaseService: DatabaseService,
    private val pokemonMapper: IPokemonMapper,
    private val pokemonKeyMapper: IPokemonKeyMapper,
) : PagingSource<Int, Pokemon>() {

    @OptIn(ExperimentalPagingApi::class)
    fun getPokemon(): Flow<PagingData<Pokemon>> {

        return Pager(
            PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = PokemonRemoteMediator(
                networkService,
                pokemonMapper,
                pokemonKeyMapper,
                databaseService
            ),
            pagingSourceFactory = { databaseService.pokemonDao().getAll() }
        ).flow
    }

    val pokemonErrorLiveData: MutableLiveData<String> = MutableLiveData()

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        //Call network for latest items and post them on success
        return try {
            val nextPageNumber = params.key ?: 0

            val response =
                getPokemonService().getList(nextPageNumber * params.loadSize, params.loadSize)

            return LoadResult.Page(
                data = pokemonMapper.map(response),
                prevKey = if (nextPageNumber > 0) nextPageNumber - 1 else null,
                nextKey = if (nextPageNumber < response.count) nextPageNumber + 1 else null
            )

        } catch (e: HttpException) {
            pokemonErrorLiveData.postValue(e.message)
            LoadResult.Error(e)

        } catch (e: Throwable) {
            e.printStackTrace()
            pokemonErrorLiveData.postValue("")
            LoadResult.Error(e)
        }
    }

    private fun getPokemonService() =
        networkService.serviceConstructor(PokemonService::class.java) as PokemonService

}