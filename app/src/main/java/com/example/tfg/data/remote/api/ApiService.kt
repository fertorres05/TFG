package com.example.tfg.data.remote.api
import com.example.tfg.data.remote.model.FlightCard
import com.example.tfg.data.remote.model.ReservationCard
import com.example.tfg.data.remote.model.ReservationRequest
import com.example.tfg.data.remote.model.User
import com.example.tfg.data.remote.model.UserResponse
import com.example.tfg.data.remote.model.UserStats
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

    //Para registrar las reservas de los usuarios
    @POST("/reservation")
    suspend fun addReservation(@Body flight: ReservationRequest): Response <ResponseBody>

    //Para obtener las reservas de los usuarios
    @GET("user/{uuid}/reservations-summary")
    suspend fun getUserReservations(@Path("uuid") uuid: String): List<ReservationCard>

    @GET("user/{uuid}/flights/")
    suspend fun getUserFlights(@Path("uuid") uuid: String): List<FlightCard>

    @GET("user/{uuid}/stats/")
    suspend fun getUserStats(@Path("uuid") uuid: String): UserStats

}
