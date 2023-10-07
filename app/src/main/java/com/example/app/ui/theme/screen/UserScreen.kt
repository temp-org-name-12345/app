package com.example.app.ui.theme.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.app.viewModel.UserViewModel


@Composable
fun UserScreen(
    userViewModel: UserViewModel,
    bottomNavGraph: @Composable () -> Unit
) {
    val locationRes by userViewModel.locationRes.observeAsState()

    Scaffold(
        bottomBar = bottomNavGraph
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

            Text("UserScreen")
            Text(text = locationRes?.toString() ?: "null")
        }
    }
}