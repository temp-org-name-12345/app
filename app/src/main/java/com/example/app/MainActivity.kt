package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
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
import com.example.app.ui.theme.screen.splashScreen.SplashScreen
import com.example.app.util.BottomNavGraph
import com.example.app.util.Screen
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

        val userViewModel = ViewModelProvider(this, UserViewModelFactory())[UserViewModel::class.java]
        val mapViewModel = ViewModelProvider(this, MapViewModelFactory(key))[MapViewModel::class.java]

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Root(
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
    userViewModel: UserViewModel,
    mapViewModel: MapViewModel,
    keyHash: String
) {
    val navController = rememberNavController()
    val user by userViewModel.user.observeAsState()

    val navToLogin = { navController.navigate(Screen.Login.route) }
    val navToMap = { navController.navigate(Screen.BottomNavScreen.Map.route) }

    Scaffold(
        bottomBar = { BottomNavGraph(navController = navController) }
    ) {

        Box(modifier = Modifier.padding(it)) {
            NavHost(navController = navController, startDestination = Screen.Splash.route) {
                composable(route = Screen.Splash.route) {
                    SplashScreen(
                        userViewModel = userViewModel,
                        keyHash = keyHash,
                        navToLogin = navToLogin,
                        navToMap = navToMap
                    )
                }

                composable(route = Screen.Login.route) {
                    LoginScreen(
                        userViewModel = userViewModel,
                        keyHash = keyHash,
                        navToMap = navToMap
                    )
                }

                composable(route = Screen.BottomNavScreen.Map.route) {
                    MapScreen(userViewModel, user)
                }

                composable(route = Screen.BottomNavScreen.Add.route) {
                    val onPostSubmitRoute = { navController.navigate(Screen.User.route) }
                    AddScreen(mapViewModel, userViewModel, onPostSubmitRoute)
                }

                composable(route = Screen.BottomNavScreen.Profile.route) {
                    ProfileScreen()
                }

                composable(route = Screen.User.route) {
                    UserScreen(userViewModel)
                }
            }
        }
    }
}


