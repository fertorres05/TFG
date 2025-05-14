package com.example.tfg.data.remote.model

data class FlightCard(
    val code_flight: String,
    val departure_date: String,
    val arrival_date: String,
    val airline_name: String,
    val departure_airport: String,
    val arrival_airport: String,
    val cost: Double,
    val persons: Int
)
