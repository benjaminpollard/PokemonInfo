package com.dd.idea.pokemoninfo.controllers

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dd.idea.pokemoninfo.models.Pokemon
import com.dd.idea.pokemoninfo.models.PokemonDetails
import com.dd.idea.pokemoninfo.models.mappers.IPokemonDetailsMapper
import com.dd.idea.pokemoninfo.models.mappers.IPokemonMapper
import com.dd.idea.pokemoninfo.services.IBaseNetworkService
import com.dd.idea.pokemoninfo.services.PokemonService
import com.dd.idea.pokemoninfo.services.database.IDatabaseService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PokemonController(
    private val networkService: IBaseNetworkService,
    private val databaseService: IDatabaseService,
    private val pokemonMapper: IPokemonMapper,
    private val pokemonDetailsMapper: IPokemonDetailsMapper
) : PagingSource<Int, Pokemon>() {

    val pokemonDetailLiveData: MutableLiveData<PokemonDetails> = MutableLiveData()
    val pokemonErrorLiveData: MutableLiveData<String> = MutableLiveData()
    val pokemonDetailErrorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getPokemonDetail(name: String, pokemonUrl: String) {

        //Check database for know items and post if found
        CoroutineScope(Dispatchers.Main).launch {
            databaseService.getItems(PokemonDetails::class.java).let { details ->
                details.flowOn(Dispatchers.IO)
                details.collect { cd ->
                    cd?.find {
                        it.name == name
                    }?.let {
                        pokemonDetailLiveData.postValue(it)
                    }
                }
            }
        }


        CoroutineScope(Dispatchers.IO).launch {
            //Call network for latest items and post them on success
            try {
                val response = getPokemonService().getPokemonDetails(pokemonUrl)

                val model = pokemonDetailsMapper.map(response)
                // update database with latest items as network is the source of truth
                pokemonDetailLiveData.postValue(model)

                CoroutineScope(Dispatchers.Main).launch {
                    databaseService.updateOrInsertItem(pokemonDetailsMapper.map(response))
                }

            } catch (e: HttpException) {
                pokemonDetailErrorLiveData.postValue(e.message)
            } catch (e: Throwable) {
                e.printStackTrace()
                pokemonDetailErrorLiveData.postValue("")
            }
        }
    }

    private fun getPokemonService() =
        networkService.serviceConstructor(PokemonService::class.java) as PokemonService

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        //Call network for latest items and post them on success
        //to use a database and Jetpack Paging would make need of Experimental so skipping for now
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
}