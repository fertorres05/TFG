package com.example.tfg.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun SquarePreview() {
    Square(title = "MOST AIRPORT", content = "Ibiza")
}

@Composable
fun Square(title: String, content: String) {
    Box(
        modifier = Modifier
            .size(175.dp) // Tamaño del cuadrado
            .background(Color(0xFFA231B5)) // Color morado

    ) {
        Column {
            Text(
                text = title,
                fontSize = 15.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp) // Aquí agregas el padding)
            )

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = content,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(8.dp) // Aquí agregas el padding
            )
        }

    }
}
