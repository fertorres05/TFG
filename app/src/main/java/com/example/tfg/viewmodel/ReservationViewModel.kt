package com.example.tfg.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.data.remote.RetrofitClient
import com.example.tfg.data.remote.model.ReservationCard
import com.example.tfg.data.remote.model.ReservationInfo
import kotlinx.coroutines.launch

class ReservationViewModel: ViewModel() {
    private val _reservation = mutableStateOf<List<ReservationCard>>(emptyList())
    val reservations: State<List<ReservationCard>> = _reservation

    private val _reservationInfo = mutableStateOf<ReservationInfo?>(null)
    val reservationInfo: State<ReservationInfo?> = _reservationInfo

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

    fun loadReservationInfo(idReservation: Int) {
        viewModelScope.launch {
            try {
                val info = RetrofitClient.api.getInfoReservation(idReservation)
                _reservationInfo.value = info
            } catch (e: Exception) {
                Log.e("ReservationViewModel", "Error loading reservation info", e)
            }
        }
    }
}
