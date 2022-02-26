package com.dd.idea.pokemoninfo.testingUtils

import org.mockito.Mockito

inline fun <reified T> mock(): T = Mockito.mock(T::class.java)
