package com.example.tfg.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.data.remote.RetrofitClient
import com.example.tfg.data.remote.model.FlightCard
import kotlinx.coroutines.launch
import androidx.compose.runtime.State


// FlightViewModel.kt
class FlightViewModel : ViewModel() {
    private val _flights = mutableStateOf<List<FlightCard>>(emptyList())
    val flights: State<List<FlightCard>> = _flights

    // Nuevo estado para el vuelo seleccionado
    var selectedFlight = mutableStateOf<FlightCard?>(null)
        private set

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

    // Método para seleccionar un vuelo
    fun selectFlight(flight: FlightCard) {
        selectedFlight.value = flight
    }
}

