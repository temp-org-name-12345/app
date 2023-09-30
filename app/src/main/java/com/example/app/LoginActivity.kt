package com.example.app

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.app.ui.theme.AppTheme
import com.example.app.util.KakaoLogin
import com.example.app.viewModel.UserViewModel
import com.example.app.viewModel.UserViewModelFactory
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val keyHash = intent.getStringExtra("keyHash") ?: ""
        val userViewModel = ViewModelProvider(this, UserViewModelFactory(keyHash))[UserViewModel::class.java]

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(userViewModel = userViewModel, keyHash = keyHash)
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun LoginScreen(
    userViewModel: UserViewModel,
    keyHash: String
) {
    LaunchedEffect(Unit) {
        userViewModel.getAppThumbnailImage()
    }

    val backgroundImages by userViewModel.thumbnailImage.observeAsState()

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

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

                    Button(onClick = {
                        coroutineScope.launch {
                            KakaoLogin.login(
                                context,
                                keyHash,
                                userViewModel
                            )
                        }
                    }) {
                        Text("Kakao Login")
                    }
                }
            }
        }
    )
}

