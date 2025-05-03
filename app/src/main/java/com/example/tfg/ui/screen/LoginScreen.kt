package com.example.tfg.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
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
import com.example.tfg.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tfg.ui.theme.Purple700
import com.example.tfg.ui.theme.SelectedField
import com.example.tfg.ui.theme.White
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.tfg.ui.theme.Amber300
import com.example.tfg.ui.theme.DarkText
import com.example.tfg.ui.theme.Pink80
import com.example.tfg.ui.theme.Purple500

@Composable
fun LoginScreen(auth: FirebaseAuth, navigateToHome: () -> Unit, navigateBack: () -> Unit) {
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
        Text("Log in", color = White, fontWeight = FontWeight.Bold, fontSize = 60.sp)

        Spacer(Modifier.height(60.dp))

        Text("Email:", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = White,
                focusedContainerColor = White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Pink80,
                cursorColor = Pink80,
                focusedTextColor = DarkText,
                unfocusedTextColor = DarkText,
                focusedLabelColor = White,
                unfocusedLabelColor = White
            ),
            placeholder = { Text("Email", color = Color.Gray) }
        )


        Spacer(Modifier.height(48.dp))

        Text("Password:", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = White,
                focusedContainerColor = White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Pink80,
                cursorColor = Pink80,
                focusedTextColor = DarkText,
                unfocusedTextColor = DarkText,
                focusedLabelColor = White,
                unfocusedLabelColor = White,
            ),
            placeholder = { Text("Password", color = Color.Gray) }
        )
        Spacer(Modifier.height(48.dp))

        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta),
            onClick = {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Navegar
                        Log.i("Login", "Login correcto")
                        navigateToHome()
                    } else {
                        //Error
                        Log.i("Login", "Login incorrecto")
                    }


                }
            }) {
            Text("Log in", color = White)
        }
    }
}

