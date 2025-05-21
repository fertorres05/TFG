package com.example.tfg.data.remote.model

data class FlightInput(
    val codeFlight: String,
    val persons: Int?,
    val cost: Double?,
    val baggage: Map<String, Int>?
)
