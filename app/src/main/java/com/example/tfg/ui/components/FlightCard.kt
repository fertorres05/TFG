package com.example.tfg.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tfg.data.remote.model.FlightCard
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

//Funcion para obtener la fecha en formato dd/MM/yyyy
@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dateStr: String): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(dateStr)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.getDefault())
        zonedDateTime.format(formatter)
    } catch (e: Exception) {
        dateStr
    }
}

// Función para obtener la hora en formato HH:mm
@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(dateStr: String): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(dateStr)
        val formatter = DateTimeFormatter.ofPattern("HH:mm").withLocale(Locale.getDefault())
        zonedDateTime.format(formatter)
    } catch (e: Exception) {
        ""
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FlightCard(flight: FlightCard, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .background(Color(0xFFD084E7))
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() } ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(flight.airline_code_iata, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(flight.code_flight, color = Color.White, fontWeight = FontWeight.Bold)
        }

        VerticalDivider()

        Row(
            modifier = Modifier.weight(2f).fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(flight.departure_airport, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text("→", color = Color.White, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(flight.arrival_airport, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        VerticalDivider()

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formatDate(flight.departure_date),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Text(
                text = formatTime(flight.departure_date),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

    }
}

@Composable
fun VerticalDivider() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(2.dp)
            .background(Color(0xFF8E24AA)) // Color violeta oscuro para las líneas divisorias
    )
}
