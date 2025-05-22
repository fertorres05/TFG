package com.example.tfg.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tfg.data.remote.model.FlightCard
import com.example.tfg.ui.components.FlightCard

@Composable
fun InfoReservationScreen(
    reservationName: String,
    totalCost: String,
    flights: List<FlightCard> // filtrados por id_reserva fuera de aquí
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCFAFF))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header con nombre de la reserva
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE090E6))
                .padding(12.dp)
        ) {
            Text(reservationName, fontWeight = FontWeight.Bold, color = Color.White)
        }

        // Coste total y cantidad de vuelos
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFB026A4))
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text("Vuelos: ${flights.size}", color = Color.White, fontWeight = FontWeight.Bold)
            Text("Coste total: $totalCost€", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Header Flights
        Box(
            modifier = Modifier
                .background(Color(0xFFE090E6))
                .padding(vertical = 8.dp, horizontal = 24.dp)
        ) {
            Text("FLIGHTS", fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de vuelos (FlightCard)
//        LazyColumn {
//            itemsIndexed(flights) { index, flight ->
//                val backgroundColor =
//                    if (index % 2 == 0) Color(0xFFD084E7) else Color(0xFFA231B5)
//                FlightCard(flight = flight, onClick = {}, backgroudColor = backgroundColor)
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//        }
    }
}
