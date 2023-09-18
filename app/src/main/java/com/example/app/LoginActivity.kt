package com.example.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.app.ui.theme.AppTheme
import com.example.app.util.KakaoLogin
import com.example.app.viewModel.MainViewModel
import kotlinx.coroutines.launch
import java.io.Serializable

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(name = "Hi", viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun LoginScreen(name: String, modifier: Modifier = Modifier, viewModel: MainViewModel) {
    Log.e("LoginScreen", "LoginScreen Composed")

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val allUserInfo by viewModel.allUserInfo.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getAllUser()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            itemsIndexed(allUserInfo ?: listOf()) { _, item ->
                Text(text = item.email)
                Text(text = item.nickname)
                Text(text = item.profileImageUrl)
            }
        }

        IconButton(
            onClick = { /* coroutineScope.launch { KakaoLogin.login(context)} */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.kakao_login_large_narrow),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(256.dp)
                    .height(80.dp)
            )
        }
    }
}


data class User(
    val nickname: String,
    val profileImageUrl: String,
    val email: String
) : Serializable

