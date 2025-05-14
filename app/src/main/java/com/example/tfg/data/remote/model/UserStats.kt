package com.example.tfg.data.remote.model


data class UserStats(
    val total_flights: Int,
    val total_spent: Double,
    val most_used_airline: String?,
    val airline_flight_count: Int?,
    val most_visited_destination: String?,
    val destination_flight_count: Int?
)