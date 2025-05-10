package com.example.tfg.ui.components


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


@Preview(showBackground = true)
@Composable
fun ReservationCard() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFD084E7)) // Color superior
    ) {
        // Parte superior: Origen - Destino
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("IBZ", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Ibiza", color = Color.White)
            }

//            Icon(
//                imageVector = Icons.Filled.Flight,
//                contentDescription = "Airplane",
//                tint = Color.White,
//                modifier = Modifier.size(32.dp)
//            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("ZGZ", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Zaragoza", color = Color.White)
            }
        }

        // Parte inferior: Fechas y código
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFA231B5)) // Color inferior
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    //Icon(Icons.Filled.Flight, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Wen, 28 May", color = Color.White)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("20:30", color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    //Icon(Icons.Filled.Flight, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Thur, 30 May", color = Color.White)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("19:30", color = Color.White)
                }

                Text("FR108KB", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

