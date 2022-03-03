package com.dd.idea.pokemoninfo.ui.main

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.dd.idea.pokemoninfo.ApplicationInstance
import com.dd.idea.pokemoninfo.R
import com.dd.idea.pokemoninfo.controllers.PokemonPagingSource
import com.dd.idea.pokemoninfo.models.Pokemon
import com.dd.idea.pokemoninfo.ui.Event

class MainViewModel constructor(private val controller: PokemonPagingSource) : ViewModel(),
    DefaultLifecycleObserver {

    val showPokemonDetailLiveData: MutableLiveData<Event<Pokemon>> = MutableLiveData()
    val toastLiveData: MutableLiveData<Event<String>> = MutableLiveData()

    val pokemonListLiveData get() = controller.getPokemon().asLiveData().cachedIn(viewModelScope)

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

    fun onPokemonSelected(pokemon: Pokemon) {
        showPokemonDetailLiveData.postValue(Event(pokemon))
    }
}