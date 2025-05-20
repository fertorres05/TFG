package com.example.tfg.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tfg.ui.components.MainScaffold
import com.example.tfg.ui.components.ReservationCard
import com.example.tfg.ui.components.Square
import com.example.tfg.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    auth: FirebaseAuth,
    navController: NavHostController,
    navigateToHome: () -> Unit,
    navigateToReservation: () -> Unit,
    navigateToFlights: () -> Unit,
    homeViewModel: HomeViewModel

) {
    val stats = homeViewModel.stats.value
    val isLoading = homeViewModel.isLoading.value
    val error = homeViewModel.error.value

    val uuid = auth.currentUser?.uid
    LaunchedEffect(uuid) {
        uuid?.let { homeViewModel.loadUserStats(it) }
    }

    MainScaffold(
        navigateToHome = navigateToHome,
        navigateToFlights = navigateToFlights,
        navigateToReservations = navigateToReservation,
        auth = auth,
        navController = navController,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Home",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .width(300.dp)
                    .background(Color(0xFFA231B5)) // Color inferior
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "NEXT TRAVEL",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp
                )
            }

//            ReservationCard(
//                reservationName = "Summer holidays",
//                flightsCount = 2,
//                totalCost = 300.0
//            )

            Spacer(modifier = Modifier.height(20.dp))

            // Fila de 4 cuadrados
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Square(
                    title = "TOTAL FLIGHTS:",
                    content = (stats?.total_flights ?: "-").toString()
                )
                Spacer(modifier = Modifier.height(20.dp))
                Square(
                    title = "MONEY SPENT: ",
                    content = "${stats?.total_spent ?: "-"}€"
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Square(
                    title = "MOST AIRLINE:",
                    content = "Airline: \n ${stats?.most_used_airline ?: "-"} \n Times: \n ${stats?.airline_flight_count ?: "-"}",


                    )
                Spacer(modifier = Modifier.height(20.dp))
                Square(

                    title = "MOST DESTINATION: ",
                    content = "Airport: \n ${stats?.most_visited_destination ?: "-"} \n Times: \n ${stats?.destination_flight_count ?: "-"}",

                    )
            }


        }
    }
}
