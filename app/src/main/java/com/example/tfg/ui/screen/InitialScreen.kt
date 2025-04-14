package com.example.tfg.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.tfg.R
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tfg.ui.theme.Purple500
import com.example.tfg.ui.theme.Purple700

@Preview
@Composable
fun InitialScreen(navigateToLogin: () -> Unit={}, navigateToSignUp: () -> Unit={}) {
    Column(
        modifier = Modifier
            .fillMaxSize() //El brush sirve para degradar el color de la pantalla
            .background(
                Brush.verticalGradient(
                    listOf(Purple500, Purple700),
                    startY = 0f,
                    endY = 800f
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.weight(1f))


        Text(
            "Record your adventures",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Free on TFG",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(1f))

        //Boton de registro
        Button(
            onClick = {navigateToSignUp()},
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta)
        ) {
            Text(
                text = "Sign up free",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        //Boton de login
        Text(text = "Log in",
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier.padding(24.dp).clickable { navigateToLogin() },
            fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.weight(1f))
    }
}
