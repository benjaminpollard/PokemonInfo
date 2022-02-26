package com.dd.idea.pokemoninfo.services

import com.dd.idea.pokemoninfo.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BaseNetworkService {
    fun serviceConstructor(ServiceToCon: Class<*>?): Any {

        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.API_URL)
            .build()
        return retrofit.create(ServiceToCon)
    }
}
