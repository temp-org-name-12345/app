package com.example.app.ui.theme.screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.app.viewModel.UserViewModel


@Composable
fun UserScreen(userViewModel: UserViewModel) {
    val submitResponse by userViewModel.submitResponse.observeAsState()

    Text("UserScreen")
    Text(text = submitResponse ?: "null")
}