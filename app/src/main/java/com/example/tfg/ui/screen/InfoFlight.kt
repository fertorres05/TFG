package com.example.tfg.ui.screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tfg.data.remote.model.LuggageItem
import com.example.tfg.ui.components.BaggageOptionList
import com.example.tfg.ui.components.MainScaffold
import com.example.tfg.ui.components.ReservationCard
import com.example.tfg.ui.components.formatDateLong
import com.example.tfg.ui.components.formatDateShort
import com.example.tfg.ui.components.formatTime
import com.example.tfg.viewmodel.FlightViewModel
import com.google.firebase.auth.FirebaseAuth
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FlightDetailScreen(
    auth: FirebaseAuth,
    navController: NavHostController,
    navigateToHome: () -> Unit,
    navigateToReservation: () -> Unit,
    navigateToFlights: () -> Unit,
    viewModel: FlightViewModel
) {
    val flight = viewModel.selectedFlight.value
    val showDeleteDialog = remember { mutableStateOf(false) }
    val showCannotDeleteDialog = remember { mutableStateOf(false) }
    val isEditing = remember { mutableStateOf(false) }

    if (flight == null) {
        Text("No se ha seleccionado ningún vuelo.")
        return
    }

    val costText = remember { mutableStateOf(flight.cost?.toString() ?: "0") }
    val passengersCount = remember { mutableStateOf(flight.persons ?: 0) }
    val baggageMap = remember {
        mutableStateMapOf<String, Int>().apply {
            flight.luggage.forEach {
                this[it.type] = it.amount
            }
            listOf(
                "Suitcase 10kg",
                "Checked baggage 10kg",
                "Checked baggage 20kg",
                "Special luggage"
            ).forEach {
                if (!containsKey(it)) put(it, 0)
            }
        }
    }

    LaunchedEffect(flight) {
        viewModel.loadReservation(flight.id_reservation)
    }

    MainScaffold(
        navigateToHome = navigateToHome,
        navigateToFlights = navigateToFlights,
        navigateToReservations = navigateToReservation,
        auth = auth,
        navController = navController
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF9F4FB))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFD084E7))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                flight.airline_name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                flight.airline_code_iata,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    flight.departure_airport,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    flight.departure_airport_name,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    flight.arrival_airport,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    flight.arrival_airport_name,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                item {
                    //DATE AND TIME
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("DEPERTURE", fontWeight = FontWeight.Bold)
                            Text(
                                text = formatDateShort(flight.departure_date),
                                color = Color.Black,
                                fontSize = 15.sp
                            )
                            Text(
                                text = formatTime(flight.departure_date),
                                color = Color.Black,
                                fontSize = 15.sp
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("╌╌╌╌╌╌2:25h ╌╌╌╌╌╌╌", fontSize = 12.sp, color = Color.Gray)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("ARRIBAL", fontWeight = FontWeight.Bold)
                            Text(
                                text = formatDateShort(flight.arrival_date),
                                color = Color.Black,
                                fontSize = 15.sp
                            )
                            Text(
                                text = formatTime(flight.arrival_date),
                                color = Color.Black,
                                fontSize = 15.sp
                            )
                        }
                    }

                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("PASSENGERS", fontWeight = FontWeight.Bold)
                            if (isEditing.value) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .border(1.dp, Color(0xFFA231B5))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clickable {
                                                if (passengersCount.value > 0) passengersCount.value--
                                            }
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "-",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFA231B5)
                                        )
                                    }
                                    Box(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            passengersCount.value.toString(),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clickable { passengersCount.value++ }
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "+",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFA231B5)
                                        )
                                    }
                                }
                            } else {
                                Text(
                                    flight.persons?.toString() ?: "No hay personas en este vuelo",
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("COST", fontWeight = FontWeight.Bold)
                            if (isEditing.value) {
                                TextField(
                                    value = costText.value,
                                    onValueChange = { costText.value = it },
                                    singleLine = true,
                                    modifier = Modifier.width(100.dp),
                                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
                                )
                            } else {
                                Text("${flight.cost ?: 0.0} €")
                            }
                        }
                    }
                }

                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("LUGGAGE", fontWeight = FontWeight.Bold)
                        if (isEditing.value) {
                            BaggageOptionList(
                                initialValues = baggageMap.toMap(),
                                onBaggageChange = { newMap ->
                                    baggageMap.clear()
                                    baggageMap.putAll(newMap)
                                }
                            )
                        } else {
                            val luggage = flight.luggage
                            if (luggage.isNullOrEmpty()) {
                                Text("No hay equipaje", textAlign = TextAlign.Center)
                            } else {
                                Column(horizontalAlignment = Alignment.Start) {
                                    luggage.forEach {
                                        Text(
                                            "${it.amount} x ${it.type}",
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("RESERVATION", fontWeight = FontWeight.Bold)
                        val reservation = viewModel.selectedReservation.value

                        reservation?.let {
                            ReservationCard(reservation = it, onClick = {
                                navController.navigate("reservationDetail/${reservation.id_reservation}")
                            })
                        } ?: Text("Cargando reserva...")

                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                if (isEditing.value) {
                                    val newCost = costText.value.toDoubleOrNull()
                                    if (newCost != null) {
                                        val updatedLuggageList = baggageMap
                                            .filter { it.value > 0 }
                                            .map { (type, amount) ->
                                                LuggageItem(
                                                    amount = amount,
                                                    type = type
                                                )
                                            }


                                        val formattedDeparture =
                                            formatDateLong(flight.departure_date)
                                        val formattedArrival = formatDateLong(flight.arrival_date)

                                        Log.d(
                                            "FlightUpdate",
                                            "Sending codeFlight: ${flight.code_flight}"
                                        )
                                        Log.d(
                                            "FlightUpdate",
                                            "Sending departureDate: ${formattedDeparture}"
                                        )
                                        Log.d(
                                            "FlightUpdate",
                                            "Sending sin formatear: ${formattedArrival}"
                                        )


                                        viewModel.updateFlightDetails(
                                            idReservation = flight.id_reservation,
                                            codeFlight = flight.code_flight,
                                            newCost = newCost,
                                            newPassengers = passengersCount.value,
                                            arrivalDate = formattedArrival,
                                            departureDate = formattedDeparture,
                                            newLuggage = updatedLuggageList
                                        )


                                    }
                                    isEditing.value = false
                                } else {
                                    isEditing.value = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA231B5))
                        ) {
                            Text(if (isEditing.value) "SAVE" else "EDIT", color = Color.White)
                        }

                        Button(
                            onClick = { showDeleteDialog.value = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("DELETE", color = Color.White)
                        }
                    }
                }
            }

            if (showDeleteDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog.value = false },
                    title = { Text("Confirmar eliminación") },
                    text = { Text("¿Estás seguro de que quieres eliminar este vuelo de la reserva?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDeleteDialog.value = false
                                val flightCount =
                                    viewModel.selectedReservation.value?.flight_count ?: 0
                                if (flightCount > 1) {
                                    viewModel.deleteFlightFromReservation(
                                        flight.id_reservation,
                                        flight.code_flight
                                    )
                                    navController.popBackStack()
                                } else {
                                    showCannotDeleteDialog.value = true
                                }
                            }
                        ) {
                            Text("Sí")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog.value = false }) {
                            Text("No")
                        }
                    }
                )
            }

            if (showCannotDeleteDialog.value) {
                AlertDialog(
                    onDismissRequest = { showCannotDeleteDialog.value = false },
                    title = { Text("No se puede eliminar") },
                    text = { Text("Este es el único vuelo en la reserva. Para eliminarlo, debes eliminar la reserva completa.") },
                    confirmButton = {
                        TextButton(onClick = { showCannotDeleteDialog.value = false }) {
                            Text("Aceptar")
                        }
                    }
                )
            }
        }
    }
}
