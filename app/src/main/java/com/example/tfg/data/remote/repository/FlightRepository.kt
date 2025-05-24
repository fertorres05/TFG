package com.example.tfg.data.remote.repository

import android.util.Log
import com.example.tfg.data.remote.RetrofitClient
import com.example.tfg.data.remote.model.FlightCard
import com.example.tfg.data.remote.model.LuggageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class FlightRepository {

    suspend fun deleteFlightFromReservation(id_reservation: Int, code_flight: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.deleteFlightFromReservation(id_reservation, code_flight)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Error al borrar vuelo: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun updateFlightDetails(
        idReservation: Int,
        codeFlight: String,
        newCost: Double,
        newPassengers: Int,
        departureDate: String,
        arrivalDate: String,
        newLuggage: List<LuggageItem>,
    ) {
        Log.d("UpdateFlight", "Luggage list to send: $newLuggage")

        RetrofitClient.api.updateFlightDetails(idReservation, codeFlight, newCost, newPassengers,departureDate,arrivalDate, newLuggage)
    }

    suspend fun getFlightByCode(
        idReservation: Int,
        codeFlight: String,
        departureDate: String,
        arrivalDate: String
    ): FlightCard {
        return RetrofitClient.api.getFlightByCode(idReservation,codeFlight, departureDate, arrivalDate)
    }


}
