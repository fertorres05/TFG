package com.example.tfg.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.res.painterResource
import com.example.tfg.R

@Preview(showBackground = true)
@Composable
fun ReservationListScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ReservationCard(
            reservationName = "Hollydais trip",
            flightsCount = 2,
            totalCost = 300.0
        )
    }
}

@Composable
fun ReservationCard(
    reservationName: String,
    flightsCount: Int,
    totalCost: Double,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFFA231B5)) // Color superior
    ) {

        //Nombre de la reserva
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(reservationName, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD084E7 )) // Color inferior
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                    // Info de vuelos
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.airplane),
                            contentDescription = "Logo",
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Flights: $flightsCount",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,

                        )
                    }

                    // Info de coste
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Image(
                            painter = painterResource(id = R.drawable.cost),
                            contentDescription = "Cost icon",
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Cost: ${"%.2f".format(totalCost)}€",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

