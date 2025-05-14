package com.example.tfg.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.tfg.ui.components.FlightCard
import com.example.tfg.ui.components.MainScaffold
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tfg.viewmodel.FlightViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun FlightScreen(
    auth: FirebaseAuth,
    navController: NavHostController,
    navigateToHome: () -> Unit,
    navigateToReservation: () -> Unit,
    navigateToFlights: () -> Unit
) {
    val flightViewModel: FlightViewModel = viewModel()
    val flights = flightViewModel.flights.value

    val userId = auth.currentUser?.uid

    LaunchedEffect(userId) {
        userId?.let {
            println("Cargando vuelos para el usuario con ID: $it")
            flightViewModel.loadFlights(it)
        }
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

            Text(
                text = "Flights",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .fillMaxHeight()
            ) {
                items(flights) { flight ->
                    FlightCard(flight)
                }
            }
            Text(
                text = "Flights",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

    }
}
