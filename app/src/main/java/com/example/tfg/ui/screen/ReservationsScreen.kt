package com.example.tfg.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tfg.ui.components.FlightCard
import com.example.tfg.ui.components.MainScaffold
import com.example.tfg.ui.components.ReservationCard
import com.example.tfg.ui.navigation.FlightDetail
import com.example.tfg.viewmodel.ReservationViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.lazy.items


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationsScreen(
    auth: FirebaseAuth,
    navController: NavHostController,
    navigateToHome: () -> Unit,
    navigateToFlights: () -> Unit,
    navigateToReservation: () -> Unit,
    navigatesToAddReservation: () -> Unit,
    viewModel: ReservationViewModel
) {

    val reservations = viewModel.reservations.value
    val userId = auth.currentUser?.uid

    LaunchedEffect(userId) {
        userId?.let {
            println("Cargando vuelos para el usuario con ID: $it")
            viewModel.loadReservations(it)
        }
    }

    MainScaffold(
        navigateToHome = navigateToHome,
        navigateToFlights = navigateToFlights,
        navigateToReservations = navigateToReservation,
        auth = auth,
        navController = navController,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigatesToAddReservation()
                    println("FAB clicked!")
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "RESERVATIONS",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(10.dp))


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxHeight()
            ) {
                items(reservations) { item ->
                    ReservationCard(reservation = item)
                }
            }


        }
    }
}
