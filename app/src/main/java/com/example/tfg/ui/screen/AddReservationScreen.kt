package com.example.tfg.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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


private fun addReservation(request: ReservationRequest) {
    println("Intentando añadir reserva múltiple")
    println("UUID: ${request.uuid}")
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitClient.api.addReservation(request)
            if (response.isSuccessful) {
                val responseString = response.body()?.string()
                println("Respuesta del servidor: $responseString")
            } else {
                println("Error: ${response.code()} - ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            println("Error: ${e.localizedMessage}")
        }
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
    data class FlightInputFormState(
        var codeFlight: String = "",
        var persons: String = "",
        var cost: String = "",
        var baggage: Map<String, Int> = emptyMap()
    )

    val flightInputs = remember { mutableStateListOf(FlightInputFormState()) }
    var reservationName by remember { mutableStateOf("") }



    MainScaffold(
        navigateToHome = navigateToHome,
        navigateToFlights = navigateToFlights,
        navigateToReservations = navigateToReservation,
        auth = auth,
        navController = navController,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val flightList = flightInputs.mapNotNull {
                    if (it.codeFlight.isNotBlank()) {
                        com.example.tfg.data.remote.model.FlightInput(
                            codeFlight = it.codeFlight,
                            persons = it.persons.toIntOrNull(),
                            cost = it.cost.toDoubleOrNull(),
                            baggage = if (it.baggage.isNotEmpty()) it.baggage else null
                        )
                    } else null
                }


                val request = ReservationRequest(
                    uuid = uuid,
                    reservation_name = reservationName,
                    flights = flightList
                )

                addReservation(request)
            }) {
                Icon(Icons.Filled.Check, contentDescription = "Add")
            }

        }

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Reservation Name:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                TextField(
                    value = reservationName,
                    onValueChange = { reservationName = it },
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
                    label = { Text("Reservation Name") }
                )

                Spacer(modifier = Modifier.height(15.dp))
            }

            itemsIndexed(flightInputs) { index, flight ->
                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Flight #${index + 1}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    if (flightInputs.size > 1) {
                        Text(
                            text = "❌",
                            color = Color.Red,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .clickable {
                                    flightInputs.removeAt(index)
                                }
                                .padding(8.dp)
                        )
                    }
                }

                TextField(
                    value = flight.codeFlight,
                    onValueChange = { flightInputs[index] = flight.copy(codeFlight = it) },
                    label = { Text("Code Flight") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextField(
                        value = flight.persons,
                        onValueChange = { flightInputs[index] = flight.copy(persons = it) },
                        label = { Text("Persons: (Opcional)") },
                        modifier = Modifier.width(150.dp)
                    )
                    TextField(
                        value = flight.cost,
                        onValueChange = { flightInputs[index] = flight.copy(cost = it) },
                        label = { Text("Cost: (Opcional)") },
                        modifier = Modifier.width(150.dp)
                    )
                }

                val expandedStates = remember { mutableStateMapOf<Int, Boolean>() }

                val isExpanded = expandedStates[index] ?: false

                Text("Luggage:")

                Text(
                    text = if (isExpanded) "Hide baggage options" else "Show baggage options",
                    fontSize = 14.sp,
                    color = Purple500,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable {
                            expandedStates[index] = !isExpanded
                        }
                        .padding(bottom = 8.dp)
                )

                if (isExpanded) {
                    BaggageOptionList { baggage ->
                        flightInputs[index] = flight.copy(baggage = baggage)
                    }
                }


            }

            item {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "+ Add another flight",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Purple500,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .clickable {
                            flightInputs.add(FlightInputFormState())
                        }
                )
            }
        }

    }
}

