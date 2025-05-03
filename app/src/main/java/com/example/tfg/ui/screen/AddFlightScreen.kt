package com.example.tfg.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tfg.ui.components.DatePickerDocked
import com.example.tfg.ui.components.MainScaffold
import com.example.tfg.ui.theme.*

@Preview
@Composable
fun AddFlightScreenPreview() {
    AddFlightScreen(
        navigateBack = {},
        navigateToHome = {},
        navigateToFlights = {}
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFlightScreen(
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToFlights: () -> Unit
) {
    var codeflight by remember { mutableStateOf("") }

    MainScaffold(
        navigateToHome = navigateToHome,
        navigateToFlights = navigateToFlights,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    println("FAB clicked!")
                }
            ) {
                Icon(Icons.Filled.Check, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Code Flight:",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            TextField(
                value = codeflight,
                onValueChange = { codeflight = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = UnselectedField,
                    focusedContainerColor = SelectedField,
                    unfocusedIndicatorColor = Black,
                    focusedIndicatorColor = Amber300,
                    cursorColor = Amber300,
                    focusedTextColor = DarkText,
                    unfocusedTextColor = DarkText,
                    focusedLabelColor = White,
                    unfocusedLabelColor = White,
                ),
                label = { Text("Code Flight") }
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Departure date:",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            DatePickerDocked()

        }
    }
}
