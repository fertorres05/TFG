package com.example.tfg.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.data.remote.RetrofitClient
import com.example.tfg.data.remote.model.FlightCard
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import com.example.tfg.data.remote.model.ReservationCard
import com.example.tfg.data.remote.repository.FlightRepository


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

    fun deleteFlightFromReservation(id_reservation: Int, codeFlight: String) {
        viewModelScope.launch {
            val result = repository.deleteFlightFromReservation(id_reservation, codeFlight)
            _deleteResult.value = result
        }
    }

    fun clearDeleteResult() {
        _deleteResult.value = null
    }
}


