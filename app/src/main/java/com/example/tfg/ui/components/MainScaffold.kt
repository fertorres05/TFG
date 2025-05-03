package com.example.tfg.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.tfg.R
import com.example.tfg.ui.theme.Purple500
import com.example.tfg.ui.theme.White
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tfg.viewmodel.UserViewModel


@Preview
@Composable
fun MainScaffoldPreview () {
    MainScaffold(
        navigateToHome ={},
        navigateToFlights = { },
        content = {},
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navigateToHome: () -> Unit,
    navigateToFlights: () -> Unit,
    floatingActionButton: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val userViewModel: UserViewModel = viewModel()
    val username by userViewModel.username.collectAsState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple500,
                    titleContentColor = White,
                    navigationIconContentColor = White,
                    actionIconContentColor = White,
                ),
                title = {
                    Text(
                        "TFG",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    Text(username)
                },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Purple500,
                contentColor = White,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = {navigateToHome()}) {
                        Icon(painterResource(id = R.drawable.home), contentDescription = "Home Screen")
                    }
                    IconButton(onClick = { navigateToFlights() }) {
                        Icon(painterResource(id = R.drawable.plane), contentDescription = "Flights Screen")
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(painterResource(id = R.drawable.money), contentDescription = "Money Screen")
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(painterResource(id = R.drawable.user), contentDescription = "User Screen")
                    }
                }
            }
        },
        floatingActionButton = {
            floatingActionButton?.invoke()
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}
