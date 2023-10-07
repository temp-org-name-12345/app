package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.screen.addScreen.AddScreen
import com.example.app.ui.theme.screen.MapScreen
import com.example.app.ui.theme.screen.ProfileScreen
import com.example.app.ui.theme.screen.UserScreen
import com.example.app.ui.theme.screen.loginScreen.LoginScreen
import com.example.app.util.BottomNavGraph
import com.example.app.util.Screen
import com.example.app.viewModel.DefaultAppViewModel
import com.example.app.viewModel.DefaultAppViewModelFactory
import com.example.app.viewModel.MapViewModel
import com.example.app.viewModel.MapViewModelFactory
import com.example.app.viewModel.UserViewModel
import com.example.app.viewModel.UserViewModelFactory
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val key = getString(R.string.KAKAO_REST_API_KEY)
        val keyHash = Utility.getKeyHash(this)
        KakaoSdk.init(this, getString(R.string.KAKAO_NATIVE_APP_KEY))

        val defaultAppViewModel = ViewModelProvider(this, DefaultAppViewModelFactory())[DefaultAppViewModel::class.java]
        val userViewModel = ViewModelProvider(this, UserViewModelFactory(key))[UserViewModel::class.java]
        val mapViewModel = ViewModelProvider(this, MapViewModelFactory(key))[MapViewModel::class.java]

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Root(
                        defaultAppViewModel = defaultAppViewModel,
                        userViewModel = userViewModel,
                        mapViewModel = mapViewModel,
                        keyHash = keyHash
                    )
                }
            }
        }
    }
}

@Composable
fun Root(
    defaultAppViewModel: DefaultAppViewModel,
    userViewModel: UserViewModel,
    mapViewModel: MapViewModel,
    keyHash: String
) {
    LaunchedEffect(Unit) {
        userViewModel.getUserByKeyHash(keyHash)
    }

    val navController = rememberNavController()

    /* STATES */
    val user by userViewModel.user.observeAsState()

    /* NAV HANDLERS */
    val navToMap = { navController.navigate(Screen.Map.route) }

    NavHost(
        navController = navController,
        startDestination = if (user == null) Screen.Login.route else Screen.Map.route
    ) {

        composable(route = Screen.Login.route) {
            LoginScreen(
                defaultAppViewModel = defaultAppViewModel,
                userViewModel = userViewModel,
                keyHash = keyHash,
                navToMap = navToMap
            )
        }

        composable(route = Screen.User.route) {
            UserScreen(userViewModel) {
                BottomNavGraph(navController = navController)
            }
        }

        composable(route = Screen.Map.route) {
            MapScreen(
                userViewModel = userViewModel,
                user = user
            )

        }

        composable(route = Screen.Add.route) {
            val onPostSubmitRoute = { navController.navigate(Screen.User.route) }

            AddScreen(
                userViewModel = userViewModel,
                mapViewModel = mapViewModel,
                onPostSubmitRoute = onPostSubmitRoute
            ) {
                BottomNavGraph(navController = navController)
            }
        }

        composable(route = Screen.Profile.route) {
            ProfileScreen {
                BottomNavGraph(navController = navController)
            }
        }

    }
}


