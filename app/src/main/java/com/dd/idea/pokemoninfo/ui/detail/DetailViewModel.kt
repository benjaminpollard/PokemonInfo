package com.dd.idea.pokemoninfo.ui.detail

import android.os.Bundle
import androidx.lifecycle.*
import com.dd.idea.pokemoninfo.ApplicationInstance
import com.dd.idea.pokemoninfo.R
import com.dd.idea.pokemoninfo.controllers.PokemonController
import com.dd.idea.pokemoninfo.controllers.StringResolverController
import com.dd.idea.pokemoninfo.models.PokemonDetails
import com.dd.idea.pokemoninfo.ui.Event

class DetailViewModel(private val arguments: Bundle?, private val controller: PokemonController, private val stringResolverController: StringResolverController) :
    ViewModel(), DefaultLifecycleObserver {

    private var pokemonUrl: String = ""
    private var pokemonName: String = ""

    val pokemonDetailLiveData: MutableLiveData<PokemonDetails>
        get() {
            return controller.pokemonDetailLiveData
        }
    val pokemonNameLiveData: MutableLiveData<String> = MutableLiveData()
    val toastLiveData: MutableLiveData<Event<String>> = MutableLiveData()
    val popBackStackLiveData: MutableLiveData<Event<Unit>> = MutableLiveData()
    val stateLiveData: MutableLiveData<State> = MutableLiveData()

    private val errorObserver : Observer<String>
    private val detailCompleteObserver : Observer<PokemonDetails>

    init {
        //handle formatting errors to be ui friendly
        errorObserver = Observer {
            if (it.isNotEmpty()) {
                toastLiveData.postValue(Event(stringResolverController.get(R.string.informed_error) + it))
            } else {
                toastLiveData.postValue(Event(stringResolverController.get(R.string.error)))
            }
            //had an error and don't have data to show
            if (pokemonDetailLiveData.value == null)
                stateLiveData.postValue(State.ERROR)
        }

        detailCompleteObserver = Observer {
            stateLiveData.postValue(State.COMPLETE)
        }

        controller.pokemonDetailErrorLiveData.observeForever(errorObserver)
        controller.pokemonDetailLiveData.observeForever(detailCompleteObserver)

    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        stateLiveData.postValue(State.LOADING)

        pokemonName = arguments?.getString(NAME) ?: ""
        pokemonUrl = arguments?.getString(URL) ?: ""

        //needs to have the url passed in or we are here incorrectly
        if (pokemonName.isEmpty() || pokemonUrl.isEmpty()) {
            toastLiveData.postValue(Event(ApplicationInstance.getContext().resources.getString(R.string.error)))
            onBackPressed()
            return
        }

        pokemonNameLiveData.postValue(pokemonName)

        getPokemonDetails()
    }

    override fun onCleared() {
        super.onCleared()
        controller.pokemonDetailErrorLiveData.removeObserver(errorObserver)
        controller.pokemonDetailLiveData.removeObserver(detailCompleteObserver)
    }

    private fun getPokemonDetails() {
        controller.getPokemonDetail(pokemonName, pokemonUrl)
    }

    fun onBackPressed() {
        popBackStackLiveData.postValue(Event(Unit))
    }

    fun onRetryPressed() {
        getPokemonDetails()
    }

    enum class State {
        LOADING,
        ERROR,
        COMPLETE
    }

    companion object {
        const val NAME = "pokemon_name"
        const val URL = "pokemon_url"
    }
}