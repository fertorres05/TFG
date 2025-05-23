package com.example.tfg.ui.screen

import android.R.attr.onClick
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.tfg.data.remote.model.ReservationCard
import com.example.tfg.ui.components.MainScaffold
import com.example.tfg.ui.components.ReservationCard
import com.example.tfg.ui.components.formatDate
import com.example.tfg.ui.components.formatTime
import com.example.tfg.viewmodel.FlightViewModel
import com.google.firebase.auth.FirebaseAuth

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


    if (flight == null) {
        Text("No se ha seleccionado ningún vuelo.")
        return
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
                .fillMaxSize()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF9F4FB))
                    .padding(16.dp)
            ) {
                // HEADER
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

                Spacer(modifier = Modifier.height(24.dp))

                // DATE AND TIME
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("DEPERTURE", fontWeight = FontWeight.Bold)
                        Text(
                            text = formatDate(flight.departure_date),
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
                            text = formatDate(flight.arrival_date),
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

                Spacer(modifier = Modifier.height(24.dp))

                // PASSENGERS & COST
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("PASSENGERS", fontWeight = FontWeight.Bold)
                        // Icon(Icons.Filled.People, contentDescription = null, tint = Color(0xFFA231B5))
                        Text(" ${flight.persons} ")
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("COST", fontWeight = FontWeight.Bold)
                        //Icon(Icons.Filled.AttachMoney, contentDescription = null, tint = Color(0xFFA231B5))
                        Text("${flight.cost} €")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // LUGGAGE & RESERVATION

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("LUGGAGE", fontWeight = FontWeight.Bold)

                    if (flight.luggage.isNotEmpty()) {
                        Column(horizontalAlignment = Alignment.Start) {
                            flight.luggage.forEach {
                                Text("${it.amount} x ${it.type}", textAlign = TextAlign.Center)
                            }
                        }
                    } else {
                        Text("No luggage", textAlign = TextAlign.Center)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
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

                // BUTTONS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA231B5))
                    ) {
                        Text("SAVE", color = Color.White)
                    }
                    Button(
                        onClick = { showDeleteDialog.value = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("DELETE", color = Color.White)
                    }

                }

                // Diálogo de confirmación
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
                                        viewModel.deleteFlightFromReservation(flight.id_reservation, flight.code_flight)
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

                // Diálogo de advertencia si no se puede eliminar
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
}
