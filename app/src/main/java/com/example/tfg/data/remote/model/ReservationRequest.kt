package com.example.tfg.data.remote.model

data class ReservationRequest(
    val codeflight: String,
    val uuid_user: String,
    val persons: Int? = null,
    val cost: Double? = null,
    val luggage: Map<String, Int>,
    val reservation_name: String
)
