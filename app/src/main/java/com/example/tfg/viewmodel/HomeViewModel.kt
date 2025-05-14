package com.example.tfg.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.data.remote.RetrofitClient
import com.example.tfg.data.remote.model.UserStats
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException



class HomeViewModel : ViewModel() {

    // Estado para manejar los datos del usuario (user stats)
    private val _stats = mutableStateOf<UserStats?>(null)
    val stats: State<UserStats?> = _stats

    // Estado de carga
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // Estado de error
    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    // Función para cargar los datos
    fun loadUserStats(uuid: String) {
        viewModelScope.launch {
            // Iniciamos el estado de carga
            _isLoading.value = true
            _error.value = null // Reseteamos el error antes de empezar la carga

            try {
                // Intentamos obtener los datos de la API
                val response = RetrofitClient.api.getUserStats(uuid)
                _stats.value = response
            } catch (e: HttpException) {
                // Si hubo un error HTTP
                _error.value = "Error en la respuesta del servidor"
                Log.e("HomeViewModel", "HttpException: ${e.message()}")
            } catch (e: IOException) {
                // Si hubo un error de red
                _error.value = "Error de conexión. Revisa tu internet."
                Log.e("HomeViewModel", "IOException: ${e.message}")
            } catch (e: Exception) {
                // En caso de cualquier otro error
                _error.value = "Ha ocurrido un error inesperado"
                Log.e("HomeViewModel", "Exception: ${e.message}")
            } finally {
                // Terminamos el estado de carga
                _isLoading.value = false
            }
        }
    }
}
