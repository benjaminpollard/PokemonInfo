package com.dd.idea.pokemoninfo.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dd.idea.pokemoninfo.controllers.PokemonController
import com.dd.idea.pokemoninfo.services.PokemonService
import com.dd.idea.pokemoninfo.testingUtils.mock
import org.junit.Rule
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

open class BasePokemonViewModelTest {
    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()

    @Rule
    @JvmField
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    protected val pokemonService: PokemonService = mock()

    @Mock
    protected val pokemonController: PokemonController = mock()

    @Mock
    protected val database: DatabaseService = mock()

}