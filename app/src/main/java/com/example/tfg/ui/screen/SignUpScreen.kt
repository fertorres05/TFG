package com.example.tfg.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tfg.R
import com.example.tfg.data.remote.RetrofitClient
import com.example.tfg.data.remote.model.User
import com.example.tfg.ui.theme.Amber300
import com.example.tfg.ui.theme.DarkText
import com.example.tfg.ui.theme.Purple500
import com.example.tfg.ui.theme.Purple700
import com.example.tfg.ui.theme.SelectedField
import com.example.tfg.ui.theme.White
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignUpScreen(auth: FirebaseAuth, navigateToHome: () -> Unit, navigateBack: () -> Unit) {
    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(
                listOf(Purple500, Purple700),
                startY = 0f,
                endY = 800f
            ))
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(Modifier.height(48.dp))
        Row() {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Back",
                tint = White,
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .size(30.dp)
                    .clickable { navigateBack() }
            )
            Spacer(Modifier.weight(1f))

        }
        Text("Sign Up", color = White, fontWeight = FontWeight.Bold, fontSize = 60.sp)

        Spacer(Modifier.height(60.dp))

        Text("Email:", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = White,
                focusedContainerColor = SelectedField,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Amber300,
                cursorColor = Amber300,
                focusedTextColor = DarkText,
                unfocusedTextColor = DarkText,
                focusedLabelColor = White,
                unfocusedLabelColor = White
            ),
            label = { Text("Email") }
        )


        Spacer(Modifier.height(48.dp))

        Text("Password:", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = White,
                focusedContainerColor = SelectedField,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Amber300,
                cursorColor = Amber300,
                focusedTextColor = DarkText,
                unfocusedTextColor = DarkText,
                focusedLabelColor = White,
                unfocusedLabelColor = White
            ),
            label = { Text("Password") }
        )
        Spacer(Modifier.height(48.dp))

        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta),
            onClick = {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val uuid = user?.uid ?: ""
                            val request = User(email = email, uuid = uuid)

                            // Llamada a tu servidor Node
                            val call = RetrofitClient.api.registerUser(request)
                            call.enqueue(object : retrofit2.Callback<Void> {
                                override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                                    if (response.isSuccessful) {
                                        Log.i("Retrofit", "Usuario registrado en PostgreSQL")
                                    } else {
                                        Log.e("Retrofit", "Error al registrar en backend: ${response.code()}")
                                    }
                                }

                                override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                                    Log.e("Retrofit", "Fallo al conectar con el backend", t)
                                }
                            })

                            Log.i("Sign Up", "Registro correcto en Firebase")
                            navigateToHome()
                        } else {
                            Log.i("Sign Up", "Registro incorrecto")
                        }
                    }
            }
        )
        {
            Text("Sign up", color = White)
        }
    }
}