package com.dd.idea.pokemoninfo.ui.detail

import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dd.idea.pokemoninfo.ApplicationInstance
import com.dd.idea.pokemoninfo.R
import com.dd.idea.pokemoninfo.controllers.PokemonController
import com.dd.idea.pokemoninfo.models.PokemonDetails
import com.dd.idea.pokemoninfo.ui.Event

class DetailViewModel(private val arguments: Bundle?, private val controller: PokemonController) :
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

    init {
        //handle formatting errors to be ui friendly
        controller.pokemonDetailErrorLiveData.observeForever {
            if (it.isNotEmpty()) {
                toastLiveData.postValue(Event(ApplicationInstance.getContext().resources.getString(R.string.informed_error) + it))
            } else {
                toastLiveData.postValue(Event(ApplicationInstance.getContext().resources.getString(R.string.error)))
            }
            //had an error and don't have data to show
            if (pokemonDetailLiveData.value == null)
                stateLiveData.postValue(State.ERROR)
        }

        controller.pokemonDetailLiveData.observeForever {
            stateLiveData.postValue(State.COMPLETE)
        }
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