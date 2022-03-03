package com.dd.idea.pokemoninfo.controllers

import androidx.lifecycle.MutableLiveData
import com.dd.idea.pokemoninfo.models.PokemonDetails
import com.dd.idea.pokemoninfo.models.mappers.IPokemonDetailsMapper
import com.dd.idea.pokemoninfo.services.DatabaseService
import com.dd.idea.pokemoninfo.services.IBaseNetworkService
import com.dd.idea.pokemoninfo.services.PokemonService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PokemonController(
    private val networkService: IBaseNetworkService,
    private val databaseService: DatabaseService,
    private val pokemonDetailsMapper: IPokemonDetailsMapper
) {

    val pokemonDetailLiveData: MutableLiveData<PokemonDetails> = MutableLiveData()
    val pokemonDetailErrorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getPokemonDetail(name: String, pokemonUrl: String) {

        //Check database for know items and post if found
        CoroutineScope(Dispatchers.IO).launch {
            databaseService.pokemonDetailDao().getAll().find {
                it.name == name
            }?.let {
                pokemonDetailLiveData.postValue(it)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            //Call network for latest items and post them on success
            try {
                val response = getPokemonService().getPokemonDetails(pokemonUrl)

                val model = pokemonDetailsMapper.map(response)
                // update database with latest items as network is the source of truth
                pokemonDetailLiveData.postValue(model)

                databaseService.pokemonDetailDao().insert(pokemonDetailsMapper.map(response))

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

}