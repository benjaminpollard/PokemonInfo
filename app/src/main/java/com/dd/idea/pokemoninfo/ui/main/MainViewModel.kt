package com.dd.idea.pokemoninfo.ui.main

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dd.idea.pokemoninfo.ApplicationInstance
import com.dd.idea.pokemoninfo.R
import com.dd.idea.pokemoninfo.controllers.PokemonController
import com.dd.idea.pokemoninfo.models.Pokemon
import com.dd.idea.pokemoninfo.ui.Event

class MainViewModel(private val controller: PokemonController) : ViewModel(),
    DefaultLifecycleObserver {

    val showPokemonDetailLiveData: MutableLiveData<Event<Pokemon>> = MutableLiveData()
    val toastLiveData: MutableLiveData<Event<String>> = MutableLiveData()

    val pokemonListLiveData: MutableLiveData<List<Pokemon>>
        get() {
            return controller.pokemonLiveData
        }

    init {
        //handle formatting errors to be ui friendly
        controller.pokemonErrorLiveData.observeForever {
            if (it.isNotEmpty()) {
                toastLiveData.postValue(Event(ApplicationInstance.getContext().resources.getString(R.string.informed_error) + it))
            } else {
                toastLiveData.postValue(Event(ApplicationInstance.getContext().resources.getString(R.string.error)))
            }
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        controller.getPokemonList()
    }

    fun onPokemonSelected(pokemon: Pokemon) {
        showPokemonDetailLiveData.postValue(Event(pokemon))
    }
}