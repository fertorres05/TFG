package com.example.tfg.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tfg.data.remote.RetrofitClient
import com.example.tfg.data.remote.model.ReservationRequest
import com.example.tfg.ui.components.MainScaffold
import com.example.tfg.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.tfg.ui.components.BaggageOptionList


val uuid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

private fun addReservation(codeflight: String, persons1: String, cost1: String, luggage: Map<String, Int>) {
    println("Intentando añadir vuelo")
    println("UUID: $uuid")
    if (codeflight.isNotEmpty()) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reservation =
                    ReservationRequest(codeflight, uuid, persons1.toInt(), cost1.toDouble(),luggage)
                val response = RetrofitClient.api.addReservation(reservation)
                if (response.isSuccessful) {
                    val responseString = response.body()?.string()
                    println("Respuesta del servidor: $responseString")
                } else {
                    println(
                        "Error en la respuesta: ${response.code()} - ${
                            response.errorBody()?.string()
                        }"
                    )
                }

            } catch (e: Exception) {
                println("Error: ${e.localizedMessage}")
            }
        }
    } else {
        println("El código de vuelo está vacío.")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReservationScreen(
    auth: FirebaseAuth,
    navController: NavHostController,
    navigateToHome: () -> Unit,
    navigateToFlights: () -> Unit,
    navigateToReservation: () -> Unit
) {
    var codeflight by remember { mutableStateOf("") }
    var persons by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    var selectedBaggage by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }


    MainScaffold(
        navigateToHome = navigateToHome,
        navigateToFlights = navigateToFlights,
        navigateToReservations = navigateToReservation,
        auth = auth,
        navController = navController,
        floatingActionButton = {
            val scope = rememberCoroutineScope() // ← Aquí
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        addReservation(codeflight, persons, cost, selectedBaggage)
                    }
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
                    unfocusedContainerColor = White,
                    focusedContainerColor = White,
                    unfocusedIndicatorColor = Pink80,
                    focusedIndicatorColor = Pink80,
                    cursorColor = Pink80,
                    focusedTextColor = DarkText,
                    unfocusedTextColor = DarkText,
                    focusedLabelColor = White,
                    unfocusedLabelColor = Color.Gray
                ),
                label = { Text("Code Flight") }
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Primer campo
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Persons:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    TextField(
                        value = persons,
                        onValueChange = { persons = it },
                        modifier = Modifier.width(120.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = White,
                            focusedContainerColor = White,
                            unfocusedIndicatorColor = Pink80,
                            focusedIndicatorColor = Pink80,
                            cursorColor = Pink80,
                            focusedTextColor = DarkText,
                            unfocusedTextColor = DarkText,
                            focusedLabelColor = Pink80,
                            unfocusedLabelColor = Color.Gray
                        ),
                        label = { Text("Number") },
                        singleLine = true
                    )
                }

                // Segundo campo
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Cost €:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    TextField(
                        value = cost,
                        onValueChange = { cost = it },
                        modifier = Modifier.width(120.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = White,
                            focusedContainerColor = White,
                            unfocusedIndicatorColor = Pink80,
                            focusedIndicatorColor = Pink80,
                            cursorColor = Pink80,
                            focusedTextColor = DarkText,
                            unfocusedTextColor = DarkText,
                            focusedLabelColor = Pink80,
                            unfocusedLabelColor = Color.Gray
                        ),
                        label = { Text("Total Cost") },
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Luggage:",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))


            BaggageOptionList { updatedMap -> selectedBaggage = updatedMap }


            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "ADD FLIGHT+",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Purple500,
                style = LocalTextStyle.current.copy(
                    textDecoration = TextDecoration.Underline
                )
            )


        }
    }
}

