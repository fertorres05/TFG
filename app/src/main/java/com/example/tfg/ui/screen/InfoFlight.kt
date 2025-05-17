package com.example.tfg.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tfg.ui.components.MainScaffold
import com.example.tfg.viewmodel.FlightViewModel
import com.google.firebase.auth.FirebaseAuth



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

    if (flight == null) {
        Text("No se ha seleccionado ningún vuelo.")
        return
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
                            "${flight.airline_name}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            "${flight.airline_code_iata}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "${flight.departure_airport}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(  "${flight.departure_airport_name}", color = Color.White)
                        }
//                Icon(
//                    imageVector = Icons.Filled.Flight,
//                    contentDescription = null,
//                    tint = Color.White,
//                    modifier = Modifier
//                        .size(40.dp)
//                        .rotate(-45f)
//                )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "${flight.arrival_airport}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text( "${flight.arrival_airport_name}", color = Color.White)
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
                        Text("10/05/2025")
                        Text("13:35 pm")
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("╌╌╌╌╌╌╌╌╌╌╌╌ 2:25h ╌╌╌╌╌╌╌", fontSize = 12.sp, color = Color.Gray)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("ARRIBAL", fontWeight = FontWeight.Bold)
                        Text("15/05/2025")
                        Text("16:00 pm")
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("LUGGAGE", fontWeight = FontWeight.Bold)
                        //Icon(Icons.Filled.Work, contentDescription = null, tint = Color(0xFFB564E3))
                        Text("5 Kg Bag\n10 Kg Bag\n20 Kg Bag", textAlign = TextAlign.Center)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("RESERVATION", fontWeight = FontWeight.Bold)
                        Text("—")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

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
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("DELETE", color = Color.White)
                    }
                }
            }
        }
    }
}
