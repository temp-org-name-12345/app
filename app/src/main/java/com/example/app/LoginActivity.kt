package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.example.app.ui.theme.AppTheme
import com.example.app.util.KakaoLogin
import com.example.app.viewModel.UserViewModel
import com.example.app.viewModel.UserViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val keyHash = intent.getStringExtra("keyHash") ?: ""
        val userViewModel = ViewModelProvider(this, UserViewModelFactory())[UserViewModel::class.java]

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(viewModel = userViewModel, keyHash = keyHash)
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    viewModel: UserViewModel,
    keyHash: String
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Button(onClick = { coroutineScope.launch { KakaoLogin.login(context, keyHash, viewModel) } }) {
        Text("Kakao Login")
    }
}

