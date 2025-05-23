package com.example.tfg.data.remote.repository

import com.example.tfg.data.remote.RetrofitClient
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

    // Puedes mover aquí también otros métodos como getFlights(), getReservationById(), etc.
}
