package com.example.tfg.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.data.remote.RetrofitClient
import com.example.tfg.data.remote.model.ReservationCard
import kotlinx.coroutines.launch

class ReservationViewModel: ViewModel() {
    private val _reservation = mutableStateOf<List<ReservationCard>>(emptyList())
    val reservations: State<List<ReservationCard>> = _reservation

    fun loadReservations(uuid: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getUserReservations(uuid)
                _reservation.value = response
            } catch (e: Exception) {
                Log.e("ReservationViewModel", "Error loading reservations", e)
            }
        }
    }

}