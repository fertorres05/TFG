package com.example.tfg.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tfg.ui.screen.AddReservationScreen
import com.example.tfg.ui.screen.FlightDetailScreen
import com.example.tfg.ui.screen.FlightScreen
import com.example.tfg.ui.screen.HomeScreen
import com.example.tfg.ui.screen.InfoReservationScreen
import com.example.tfg.ui.screen.InitialScreen
import com.example.tfg.ui.screen.LoginScreen
import com.example.tfg.ui.screen.ReservationsScreen
import com.example.tfg.ui.screen.SignUpScreen
import com.example.tfg.viewmodel.FlightViewModel
import com.example.tfg.viewmodel.HomeViewModel
import com.example.tfg.viewmodel.ReservationViewModel
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationWrapper(auth: FirebaseAuth) {
    val navController = rememberNavController()
    val currentUser = auth.currentUser // Obtener el usuario actual
    val homeViewModel: HomeViewModel = viewModel()
    val flightViewModel: FlightViewModel = viewModel()
    val reservationsViewModel: ReservationViewModel = viewModel()


    // Si el usuario está logueado, navega a Home, sino a Initial (Inicio)
    val startDestination = if (currentUser != null) Home else Initial
    //val startDestination = Initial

    NavHost(navController = navController, startDestination = startDestination) {
        composable<Initial> {
            InitialScreen(
                navigateToLogin = { navController.navigate(Login) },
                navigateToSignUp = { navController.navigate(SignUp) }
            )
        }
        composable<Login> {
            LoginScreen(
                auth = auth,
                navigateToHome = { navController.navigate(Home) },
                navigateBack = {
                    navController.navigate(Initial) {
                        popUpTo(Initial) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<SignUp> {
            SignUpScreen(
                auth = auth, navigateToHome = { navController.navigate(Home) },
                navigateBack = {
                    navController.navigate(Initial) {
                        popUpTo(Initial) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<Home> {
            HomeScreen(
                auth = auth,
                navController = navController,
                navigateToHome = { navController.navigate(Home) },
                navigateToReservation = { navController.navigate(Reservations) },
                navigateToFlights = { navController.navigate(Flights) },
                homeViewModel = homeViewModel
            )
        }
        composable<Reservations> {
            ReservationsScreen(
                auth = auth,
                navController = navController,
                navigateToHome = { navController.navigate(Home) },
                navigateToFlights = { navController.navigate(Flights) },
                navigateToReservation = { navController.navigate(Reservations) },
                navigatesToAddReservation = { navController.navigate(AddReservation) },
                viewModel = reservationsViewModel
            )
        }

        composable("reservationDetail/{reservationId}") { backStackEntry ->
            val reservationId = backStackEntry.arguments?.getString("reservationId")?.toIntOrNull()

            // Disparar la carga solo cuando reservationId no sea nulo
            LaunchedEffect(reservationId) {
                reservationId?.let {
                    reservationsViewModel.loadReservationInfo(it)
                    reservationsViewModel.loadReservationInfo(it)
                }
            }

            val reservationInfo = reservationsViewModel.reservationInfo.value

            if (reservationInfo != null) {
                InfoReservationScreen(
                    reservation = reservationInfo,
                    auth = auth,
                    navController = navController,
                    navigateToHome = { navController.navigate(Home) },
                    navigateToFlights = { navController.navigate(Flights) },
                    navigateToReservation = { navController.navigate(Reservations) },
                    flightviewModel = flightViewModel,
                    reservationViewModel = reservationsViewModel,
                )
            } else {
                Text("Cargando reserva...")
            }
        }


        composable<AddReservation> {
            AddReservationScreen(
                auth = auth,
                navController = navController,
                navigateToHome = { navController.navigate(Home) },
                navigateToReservation = { navController.navigate(Reservations) },
                navigateToFlights = { navController.navigate(Flights) }
            )
        }

        composable<Flights> {
            FlightScreen(
                auth = auth,
                navController = navController,
                navigateToHome = { navController.navigate(Home) },
                navigateToReservation = { navController.navigate(Reservations) },
                navigateToFlights = { navController.navigate(Flights) },
                viewModel = flightViewModel
            )
        }

        composable<FlightDetail> {
            FlightDetailScreen(
                auth = auth,
                navController = navController,
                navigateToHome = { navController.navigate(Home) },
                navigateToReservation = { navController.navigate(Reservations) },
                navigateToFlights = { navController.navigate(Flights) },
                viewModel = flightViewModel
            )
        }


    }
}

