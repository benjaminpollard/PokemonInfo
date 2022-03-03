package com.dd.idea.pokemoninfo

import android.app.Application
import androidx.room.Room
import com.dd.idea.pokemoninfo.controllers.PokemonController
import com.dd.idea.pokemoninfo.controllers.PokemonPagingSource
import com.dd.idea.pokemoninfo.models.mappers.*
import com.dd.idea.pokemoninfo.services.BaseNetworkService
import com.dd.idea.pokemoninfo.services.DatabaseService
import com.dd.idea.pokemoninfo.services.IBaseNetworkService
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

    lateinit var databaseService : DatabaseService

    @JvmStatic
    fun start(myApplication: Application) {
        databaseService = Room.databaseBuilder(myApplication, DatabaseService::class.java, "pokemonDB")
            .fallbackToDestructiveMigration()
            .build()

        startKoin {
            androidContext(myApplication)
            modules(listOf(viewModelsModule, controllerModule, servicesModule, mappersModule))
        }
    }

    fun service(): Koin {
        return GlobalContext.get()
    }

    private val controllerModule = module {
        factory { PokemonController(get(), databaseService, get()) }
        factory { PokemonPagingSource(get(), databaseService, get(), get()) }

    }

    private val servicesModule = module {
        single { BaseNetworkService() } bind IBaseNetworkService::class
    }

    private val viewModelsModule = module {
        viewModel { MainViewModel(get()) }
        viewModel { parameters -> DetailViewModel(arguments = parameters.get(), get()) }
    }

    private val mappersModule = module {
        single { PokemonMapper() } bind IPokemonMapper::class
        single { PokemonDetailsMapper() } bind IPokemonDetailsMapper::class
        single { PokemonKeyMapper() } bind IPokemonKeyMapper::class
    }
}