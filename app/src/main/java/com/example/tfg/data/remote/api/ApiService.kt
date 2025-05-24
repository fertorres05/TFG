package com.example.tfg.data.remote.api

import com.example.tfg.data.remote.model.FlightCard
import com.example.tfg.data.remote.model.LuggageItem
import com.example.tfg.data.remote.model.ReservationCard
import com.example.tfg.data.remote.model.ReservationInfo
import com.example.tfg.data.remote.model.ReservationRequest
import com.example.tfg.data.remote.model.User
import com.example.tfg.data.remote.model.UserResponse
import com.example.tfg.data.remote.model.UserStats
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Endpoint para el registro de usuario
    @POST("registerUser")
    fun registerUser(@Body request: User): Call<Void>

    //Para obtener informacion del usuario
    @GET("getUser/{uuid}")
    suspend fun getUser(@Path("uuid") uuid: String): UserResponse

    //Para registrar las reservas de los usuarios
    @POST("/reservation")
    suspend fun addReservation(@Body flight: ReservationRequest): Response<ResponseBody>


    //Obtiene la informacion detallada de una reserva del usuario
    @GET("reservation/{id_reservation}")
    suspend fun getInfoReservation(@Path("id_reservation") id_reservation: Int): ReservationInfo

    //Para obtener las reservas de los usuarios
    @GET("user/{uuid}/reservations-summary")
    suspend fun getUserReservations(@Path("uuid") uuid: String): List<ReservationCard>

    //Obtiene los vuelos del usuario
    @GET("user/{uuid}/flights/")
    suspend fun getUserFlights(@Path("uuid") uuid: String): List<FlightCard>

    @GET("flights/{idReservation}/{code}/{departure}/{arrival}")
    suspend fun getFlightByCode(
        @Path("idReservation") idReservation: Int,
        @Path("code") code: String,
        @Path("departure") departure: String,
        @Path("arrival") arrival: String
    ): FlightCard



    @PUT("flight/update")
    suspend fun updateFlightDetails(
        @Query("idReservation") idReservation: Int,
        @Query("codeFlight") codeFlight: String,
        @Query("cost") cost: Double,
        @Query("passengers") passengers: Int,
        @Query("departureDate") departureDate: String,
        @Query("arrivalDate") arrivalDate: String,
        @Body luggagelist: List<LuggageItem> // sin fechas aquí
    )

    //Obtiene la card de la reserva a la que pertenece el usuario
    @GET("user/flight/{id_reservation}")
    suspend fun getReservationById(@Path("id_reservation") id_reservation: Int): ReservationCard

    @DELETE("reservation/{id_reservation}/flight/{code_flight}")
    suspend fun deleteFlightFromReservation(
        @Path("id_reservation") id_reservation: Int,
        @Path("code_flight") codeFlight: String
    ): Response<Unit>

    //Obtiene las estadisticas generales del usuario
    @GET("user/{uuid}/stats/")
    suspend fun getUserStats(@Path("uuid") uuid: String): UserStats


}
