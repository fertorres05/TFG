package com.example.tfg.data.remote

import com.example.tfg.data.remote.api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    //private const val BASE_URL = "http://192.168.1.40:3000/" // Para Movil
    private const val BASE_URL = "http://10.0.2.2:3000/" // Para emulador Android

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}