package com.example.tfg.data.remote.model

data class ReservationInfo(
    val reservation_name: String,
    val flight_count: Int,
    val total_cost: Double,
    val flights: List<FlightCard>
)
