package com.example.tfg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
class UserViewModel : ViewModel() {

    private val _username = MutableStateFlow("Cargando username...")
    val username: StateFlow<String> = _username

    init {
        loadUser()
    }

    private fun loadUser() {
        val uuid = FirebaseAuth.getInstance().currentUser?.uid
        println("UUID: $uuid")
        if (uuid != null) {
            viewModelScope.launch {
                try {
                    val userResponse = RetrofitClient.api.getUser(uuid) // <- directo, suspend
                    _username.value = userResponse.username ?: "Usuario no encontrado"
                } catch (e: Exception) {
                    _username.value = "Fallo al conectar: ${e.localizedMessage}"
                }
            }
        } else {
            _username.value = "No logueado"
        }
    }
}
