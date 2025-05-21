package com.example.tfg.data.remote.model

data class ReservationRequest(
    val uuid: String,
    val reservation_name: String,
    val flights: List<FlightInput>
)
