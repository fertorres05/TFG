package com.example.tfg.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tfg.R
import com.example.tfg.data.remote.model.ReservationInfo
import com.example.tfg.ui.components.FlightCard
import com.example.tfg.ui.components.MainScaffold
import com.example.tfg.ui.navigation.FlightDetail
import com.example.tfg.viewmodel.FlightViewModel
import com.example.tfg.viewmodel.ReservationViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun InfoReservationScreen(
    reservation: ReservationInfo,
    auth: FirebaseAuth,
    navController: NavHostController,
    navigateToHome: () -> Unit,
    navigateToReservation: () -> Unit,
    navigateToFlights: () -> Unit,
    flightviewModel: FlightViewModel,
    reservationViewModel: ReservationViewModel

) {
    val showDeleteDialog = remember { mutableStateOf(false) }

    MainScaffold(
        navigateToHome = navigateToHome,
        navigateToFlights = navigateToFlights,
        navigateToReservations = navigateToReservation,
        auth = auth,
        navController = navController,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFCFAFF))
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header con nombre de la reserva
            Log.d("INFO_RESERVATION", "ID de la reserva: ${reservation.id_reservation}")

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFA231B5))
                    .padding(12.dp)
            ) {
                Text(
                    reservation.reservation_name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Coste total y cantidad de vuelos
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD084E7))
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.airplane),
                        contentDescription = "Logo",
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Flights: " + reservation.flight_count,
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
                        text = "Cost: " + reservation.total_cost + "€",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFE090E6))
                        .padding(vertical = 8.dp, horizontal = 24.dp)
                ) {
                    Text("FLIGHTS", fontWeight = FontWeight.Bold, color = Color.White)
                }

                Button(
                    onClick = { showDeleteDialog.value = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("DELETE", color = Color.White)
                }
            }

            // Header Flights

            Spacer(modifier = Modifier.height(16.dp))
            println("Reservation completa: $reservation")


            LazyColumn {
                itemsIndexed(reservation.flights) { index, flight ->
                    val backgroundColor =
                        if (index % 2 == 0) Color(0xFFD084E7) else Color(0xFFA231B5)

                    val dividerColor =
                        if (index % 2 == 0) Color(0xFFA231B5) else Color(0xFFD084E7)

                    // Asegúrate de tener el FlightCard correcto importado
                    FlightCard(
                        flight = flight,
                        onClick = {
                            flightviewModel.selectFlight(flight)
                            navController.navigate(FlightDetail)
                        },
                        backgroudColor = backgroundColor,
                        dividerColor = dividerColor,
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

        }
    }
    if (showDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog.value = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro que quieres borrar esta reserva y todos sus vuelos relacionadis?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog.value = false
                        reservationViewModel.deleteReservation(
                                reservation.id_reservation,
                            )
                            navController.popBackStack()
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
}
