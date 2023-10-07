package com.example.app.ui.theme.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProfileScreen(
    bottomNavGraph: @Composable () -> Unit
) {

    Scaffold(
        bottomBar = bottomNavGraph
    ) { innerPadding ->

        Box(modifier = Modifier.padding(innerPadding)) {
            Text(text = "ProfileScreen")
        }
    }
}