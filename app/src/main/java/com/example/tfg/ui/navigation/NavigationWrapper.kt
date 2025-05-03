package com.example.tfg.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tfg.ui.screen.AddFlightScreen
import com.example.tfg.ui.screen.FlightsScreen
import com.example.tfg.ui.screen.HomeScreen
import com.example.tfg.ui.screen.InitialScreen
import com.example.tfg.ui.screen.LoginScreen
import com.example.tfg.ui.screen.SignUpScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavigationWrapper(auth: FirebaseAuth) {
    val navController = rememberNavController()
    val currentUser = auth.currentUser // Obtener el usuario actual

    // Si el usuario está logueado, navega a Home, sino a Initial (Inicio)
    //val startDestination = if (currentUser != null) Home else Initial
    val startDestination = Initial

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
                navigateBack = { navController.navigate(Initial){popUpTo(Initial){inclusive = true}} }
            )
        }
        composable<SignUp> {
            SignUpScreen(
                auth = auth, navigateToHome = { navController.navigate(Home) },
                navigateBack = { navController.navigate(Initial){popUpTo(Initial){inclusive = true}} }
            )
        }
        composable<Home> {
            HomeScreen(
                navigateBack = {
                    auth.signOut() // Cerramos sesión en Firebase
                    navController.navigate(Initial) {
                        popUpTo(0) { inclusive = true } // Eliminamos TODO el stack
                    }
                },
                navigateToHome = { navController.navigate(Home) },
                navigateToFlights = { navController.navigate(Flights) }
            )
        }
        composable<Flights> {
            FlightsScreen(
                navigateBack = { navController.navigate(Initial){popUpTo(Initial){inclusive = true}} },
                navigateToHome = { navController.navigate(Home) },
                navigateToFlights = { navController.navigate(Flights) },
                navigatesToAddFlight = { navController.navigate(AddFlight) }
            )
        }
        composable<AddFlight> {
            AddFlightScreen(
                navigateBack = { navController.navigate(Flights) },
                navigateToHome = { navController.navigate(Home) },
                navigateToFlights = { navController.navigate(Flights) }
            )
        }

    }
}

