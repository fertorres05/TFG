package com.example.tfg.data.remote.model

data class FlightRequest(
    val codeflight: String,
    val uuid_user: String,
    val persons: Int? = null,
    val cost: Double? = null
)
