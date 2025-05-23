package com.example.tfg.data.remote.model

data class FlightCard(
    val id_reservation: Int,
    val code_flight: String,
    val departure_date: String,
    val arrival_date: String,
    val airline_code_iata: String,
    val airline_name: String,
    val departure_airport: String,
    val departure_airport_name: String,
    val arrival_airport: String,
    val arrival_airport_name: String,
    val cost: Double,
    val persons: Int?,
    val luggage: List<LuggageItem>
)
