package com.example.tfg.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tfg.ui.theme.DarkText
import com.example.tfg.ui.theme.Pink80

@Composable
fun BaggageOptionList(
    initialValues: Map<String, Int> = emptyMap(),
    onBaggageChange: (Map<String, Int>) -> Unit
) {
    val baggageState = remember { mutableStateMapOf<String, Int>().apply { putAll(initialValues) } }

    // Lista de tipos de equipaje disponibles
    val types = listOf("Suitcase 10kg", "Checked baggage 10kg", "Checked baggage 20kg", "Special luggage")

    Column {
        types.forEach { type ->
            val count = baggageState[type] ?: 0
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(type, modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (count > 0) {
                            baggageState[type] = count - 1
                            onBaggageChange(baggageState.toMap())
                        }
                    }) {
                        Text("-", fontSize = 20.sp)
                    }
                    Text(count.toString(), modifier = Modifier.padding(horizontal = 8.dp))
                    IconButton(onClick = {
                        baggageState[type] = count + 1
                        onBaggageChange(baggageState.toMap())
                    }) {
                        Text("+", fontSize = 20.sp)
                    }
                }
            }
        }
    }
}


@Composable
fun BaggageItem(
    label: String,
    count: Int,
    onCountChange: (Int) -> Unit
) {
    val lineColor = Pink80
    val backgroundColor = White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .border(1.dp, lineColor, RoundedCornerShape(8.dp))
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = DarkText
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .border(1.dp, lineColor)
        ) {
            Box(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true)
                    ) { onCountChange(count + 1) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = lineColor)
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(24.dp)
                    .background(lineColor)
            )

            Box(
                modifier = Modifier.padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(count.toString(), fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(24.dp)
                    .background(lineColor)
            )

            Box(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true)
                    ) {
                        if (count > 0) onCountChange(count - 1)
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = lineColor)
            }
        }
    }
}
