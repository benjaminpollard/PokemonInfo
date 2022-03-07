package com.dd.idea.pokemoninfo.controllers

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dd.idea.pokemoninfo.models.Pokemon
import com.dd.idea.pokemoninfo.models.mappers.IPokemonKeyMapper
import com.dd.idea.pokemoninfo.models.mappers.IPokemonMapper
import com.dd.idea.pokemoninfo.services.DatabaseService
import com.dd.idea.pokemoninfo.services.IBaseNetworkService
import kotlinx.coroutines.flow.Flow

class PokemonPagingSource(
        private val networkService: IBaseNetworkService,
        private val databaseService: DatabaseService,
        private val pokemonMapper: IPokemonMapper,
        private val pokemonKeyMapper: IPokemonKeyMapper,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getPokemon(): Flow<PagingData<Pokemon>> {

        return Pager(
                PagingConfig(
                        pageSize = 20,
                        enablePlaceholders = false
                ),
                //cant Use DI because of ExperimentalPagingApi and koin not playing nice
                remoteMediator = PokemonRemoteMediator(
                        networkService,
                        pokemonMapper,
                        pokemonKeyMapper,
                        databaseService
                ),
                pagingSourceFactory = { databaseService.pokemonDao().getAll() }
        ).flow
    }
}