package com.example.tfg.data.remote.repository

import com.example.tfg.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReservationRepository {

    suspend fun deleteReservation(id_reservation: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.deleteReservation(id_reservation)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Error al borrar vuelo: ${response.code()}"))
                }
            } catch (e: java.lang.Exception) {
                Result.failure(e)
            }
        }
    }

}