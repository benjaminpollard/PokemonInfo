package com.dd.idea.pokemoninfo.ui.main

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.dd.idea.pokemoninfo.controllers.PokemonPagingSource
import com.dd.idea.pokemoninfo.models.Pokemon
import com.dd.idea.pokemoninfo.ui.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class MainViewModel constructor(private val controller: PokemonPagingSource) : ViewModel(),
    DefaultLifecycleObserver {

    val showPokemonDetailLiveData: MutableLiveData<Event<Pokemon>> = MutableLiveData()

    val pokemonListLiveData
        get() = controller.getPokemon().flowOn(Dispatchers.IO).asLiveData().cachedIn(viewModelScope)

    fun onPokemonSelected(pokemon: Pokemon) {
        showPokemonDetailLiveData.postValue(Event(pokemon))
    }
}