package com.example.tfg.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tfg.data.remote.model.FlightCard
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateShort(dateString: String): String {
    val odt = OffsetDateTime.parse(dateString)
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return odt.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(dateString: String): String {
    val odt = OffsetDateTime.parse(dateString)
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return odt.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateLong(dateString: String): String {
    val odt = OffsetDateTime.parse(dateString)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return odt.format(formatter)
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FlightCard(
    flight: FlightCard,
    onClick: () -> Unit,
    backgroudColor: Color,
    dividerColor: Color
) {
    Row(
        modifier = Modifier
            .background(backgroudColor)
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                flight.airline_code_iata,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                flight.code_flight,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        VerticalDivider(dividerColor)

        Row(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                flight.departure_airport,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("→", color = Color.White, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                flight.arrival_airport,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        VerticalDivider(dividerColor)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text =  formatDateShort(flight.departure_date),
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
fun VerticalDivider(dividerColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(2.dp)
            .background(dividerColor)
    )
}
