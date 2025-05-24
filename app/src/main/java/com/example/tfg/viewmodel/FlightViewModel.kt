package com.example.tfg.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.data.remote.RetrofitClient
import com.example.tfg.data.remote.model.FlightCard
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import com.example.tfg.data.remote.model.LuggageItem
import com.example.tfg.data.remote.model.ReservationCard
import com.example.tfg.data.remote.repository.FlightRepository
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


// FlightViewModel.kt
class FlightViewModel : ViewModel() {

    private val repository = FlightRepository()

    private val _flights = mutableStateOf<List<FlightCard>>(emptyList())
    val flights: State<List<FlightCard>> = _flights

    var selectedFlight = mutableStateOf<FlightCard?>(null)
        private set

    val selectedReservation = mutableStateOf<ReservationCard?>(null)

    private val _deleteResult = mutableStateOf<Result<Unit>?>(null)
    val deleteResult: State<Result<Unit>?> = _deleteResult

    fun loadFlights(uuid: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getUserFlights(uuid)
                _flights.value = response
            } catch (e: Exception) {
                Log.e("FlightViewModel", "Error loading flights", e)
            }
        }
    }

    fun loadReservation(reservationId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getReservationById(reservationId)
                selectedReservation.value = response
            } catch (e: Exception) {
                Log.e("FlightViewModel", "Error loading reservation", e)
            }
        }
    }

    fun selectFlight(flight: FlightCard) {
        selectedFlight.value = flight
    }

    fun loadFlight(
        idReservation: Int,
        codeFlight: String,
        departureDate: String,
        arrivalDate: String
    ) {
        viewModelScope.launch {
            try {
                val flight = repository.getFlightByCode(idReservation,codeFlight, departureDate, arrivalDate)
                selectedFlight.value = flight
            } catch (e: Exception) {
                Log.e("FlightViewModel", "Error loading flight", e)
            }
        }
    }


    fun deleteFlightFromReservation(id_reservation: Int, codeFlight: String) {
        viewModelScope.launch {
            val result = repository.deleteFlightFromReservation(id_reservation, codeFlight)
            _deleteResult.value = result
        }
    }

    fun updateFlightDetails(
        idReservation: Int,
        codeFlight: String,
        newCost: Double,
        newPassengers: Int,
        newLuggage: List<LuggageItem>,
        departureDate: String,
        arrivalDate: String
    ) {
        viewModelScope.launch {

            try {
                repository.updateFlightDetails(
                    idReservation,
                    codeFlight,
                    newCost,
                    newPassengers,
                    departureDate,
                    arrivalDate,
                    newLuggage
                )
                // Podrías volver a cargar la reserva si quieres reflejar los cambios en UI
                loadReservation(idReservation)
                loadFlight(idReservation,codeFlight, departureDate, arrivalDate)

            } catch (e: Exception) {
                Log.e("FlightViewModel", "Error updating flight", e)
            }
        }
    }
}


