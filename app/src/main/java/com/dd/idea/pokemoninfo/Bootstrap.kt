package com.dd.idea.pokemoninfo

import android.app.Application
import com.dd.idea.pokemoninfo.controllers.PokemonController
import com.dd.idea.pokemoninfo.models.mappers.IPokemonDetailsMapper
import com.dd.idea.pokemoninfo.models.mappers.IPokemonMapper
import com.dd.idea.pokemoninfo.models.mappers.PokemonDetailsMapper
import com.dd.idea.pokemoninfo.models.mappers.PokemonMapper
import com.dd.idea.pokemoninfo.services.BaseNetworkService
import com.dd.idea.pokemoninfo.services.IBaseNetworkService
import com.dd.idea.pokemoninfo.services.database.DatabaseService
import com.dd.idea.pokemoninfo.services.database.IDatabaseService
import com.dd.idea.pokemoninfo.ui.detail.DetailViewModel
import com.dd.idea.pokemoninfo.ui.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.Koin
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module

object Bootstrap {

    @JvmStatic
    fun start(myApplication: Application) {

        startKoin {
            androidContext(myApplication)
            modules(listOf(viewModelsModule, controllerModule, servicesModule, mappersModule))
        }
    }

    fun service(): Koin {
        return GlobalContext.get()
    }

    private val controllerModule = module {
        factory { PokemonController(get(), get(), get(), get()) }
    }

    private val servicesModule = module {
        single { DatabaseService() } bind IDatabaseService::class
        single { BaseNetworkService() } bind IBaseNetworkService::class
    }

    private val viewModelsModule = module {
        viewModel { MainViewModel(get()) }
        viewModel { parameters -> DetailViewModel(arguments = parameters.get(), get()) }
    }

    private val mappersModule = module {
        single { PokemonMapper() } bind IPokemonMapper::class
        single { PokemonDetailsMapper() } bind IPokemonDetailsMapper::class
    }
}