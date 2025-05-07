package com.example.tfg.data.remote.api
import com.example.tfg.data.remote.model.FlightRequest
import com.example.tfg.data.remote.model.User
import com.example.tfg.data.remote.model.UserResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    // Endpoint para el registro de usuario
    @POST("registerUser")
    fun registerUser(@Body request: User): Call<Void>

    //Para obtener informacion del usuario
    @GET("getUser/{uuid}")
    suspend fun getUser(@Path("uuid") uuid: String): UserResponse

    @POST("/reservation")
    suspend fun addFlight(@Body flight: FlightRequest): Response <ResponseBody>
}
