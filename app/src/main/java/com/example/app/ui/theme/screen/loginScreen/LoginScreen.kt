package com.example.app.ui.theme.screen.loginScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.app.model.User
import com.example.app.util.KakaoLogin
import com.example.app.viewModel.DefaultAppViewModel
import com.example.app.viewModel.UserViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@OptIn(ExperimentalPagerApi::class)
@Composable
fun LoginScreen(
    defaultAppViewModel: DefaultAppViewModel,
    userViewModel: UserViewModel,
    keyHash: String,
    navToMap: () -> Unit
) {
    LaunchedEffect(Unit) {
        defaultAppViewModel.getAppThumbnailImage()
    }

    val backgroundImages by defaultAppViewModel.thumbnailImage.observeAsState()

    val context = LocalContext.current
    val saveAndNavToMap = {
        user: User -> userViewModel.saveUser(user)
        navToMap()
    }

    Scaffold(
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {

                backgroundImages?.let { images ->
                    HorizontalPager(
                        modifier = Modifier.fillMaxSize(),
                        count = images.size
                    ) { pageIndex ->
                        AsyncImage(
                            model = images[pageIndex],
                            contentDescription = null
                        )
                    }

                    Button(onClick = { KakaoLogin().login(context, keyHash, saveAndNavToMap) }) {
                        Text("Kakao Login")
                    }
                }
            }
        }
    )
}