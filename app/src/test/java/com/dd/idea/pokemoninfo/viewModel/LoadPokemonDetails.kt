package com.dd.idea.pokemoninfo.viewModel

import android.os.Bundle
import androidx.lifecycle.Observer
import com.dd.idea.pokemoninfo.testingUtils.mock
import com.dd.idea.pokemoninfo.ui.detail.DetailViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*


class LoadPokemonDetails : BasePokemonViewModelTest() {
    private lateinit var viewModel: DetailViewModel

    @Mock
    val itemsObserver: Observer<DetailViewModel.State> =
        mock()


    @Before
    open fun setUp() {
        val bundle = mock(Bundle::class.java).apply {
            `when`(getString(DetailViewModel.NAME)).thenReturn("ben")
            `when`(getString(DetailViewModel.URL)).thenReturn("pollard")
        }

        `when`(pokemonController.pokemonDetailErrorLiveData).thenReturn(mock())
        `when`(pokemonController.pokemonDetailLiveData).thenReturn(mock())

        viewModel = DetailViewModel(bundle, pokemonController)
    }

    @Test
    fun `Should Tell view to show loading when starting Pokemons Detail`() {
        val captor = ArgumentCaptor.forClass(DetailViewModel.State::class.java)

        viewModel.stateLiveData.observeForever(itemsObserver)
        viewModel.onCreate(mock())

        captor.run {
            verify(itemsObserver, times(1)).onChanged(capture())
            assertEquals(DetailViewModel.State.LOADING, value)
        }
    }

}