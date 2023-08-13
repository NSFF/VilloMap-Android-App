package com.example.villomap.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    val villoBaseUrl : String = "https://data.mobility.brussels"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(villoBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // converter factory to convert JSON object to Java object
            .build()
    }
}