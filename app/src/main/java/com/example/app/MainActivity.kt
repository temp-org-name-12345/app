package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.screen.addLocation.AddScreen
import com.example.app.ui.theme.screen.MapScreen
import com.example.app.ui.theme.screen.ProfileScreen
import com.example.app.ui.theme.screen.UserScreen
import com.example.app.viewModel.MapViewModel
import com.example.app.viewModel.MapViewModelFactory
import com.example.app.viewModel.UserViewModel
import com.example.app.viewModel.UserViewModelFactory
import com.kakao.sdk.common.KakaoSdk


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val keyHash = KakaoSdk.keyHash
        val key = getString(R.string.KAKAO_REST_API_KEY)

        val userViewModel = ViewModelProvider(this, UserViewModelFactory(keyHash))[UserViewModel::class.java]
        val mapViewModel = ViewModelProvider(this, MapViewModelFactory(key))[MapViewModel::class.java]

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Root(
                        userViewModel = userViewModel,
                        mapViewModel = mapViewModel
                    )
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object User : Screen("user")

    sealed class BottomNavScreen(
        val route: String,
        val title: String,
        val icon: ImageVector,
        val badgeCount: Int
    ) {
        object Map : BottomNavScreen("map", "Map", Icons.Filled.Home, 3)
        object Add : BottomNavScreen("add", "Add", Icons.Filled.Add, 5)
        object Profile : BottomNavScreen("profile", "Profile", Icons.Filled.AccountCircle, 5)

        companion object {
            val BottomItems = listOf(Map, Add, Profile)
        }
    }
}



@Composable
fun Root(
    userViewModel: UserViewModel,
    mapViewModel: MapViewModel,
) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        userViewModel.getUserByKeyHash()
    }

    val user by userViewModel.user.observeAsState()

    Scaffold(
        bottomBar = { BottomNavGraph(navController = navController) }
    ) {

        Box(modifier = Modifier.padding(it)) {
            NavHost(navController = navController, startDestination = Screen.BottomNavScreen.Map.route) {
                composable(route = Screen.BottomNavScreen.Map.route) {
                    MapScreen(userViewModel, user)
                }

                composable(route = Screen.BottomNavScreen.Add.route) {
                    AddScreen(user, mapViewModel, userViewModel)
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

@Composable
fun BottomNavGraph(navController: NavController) {
    val items = Screen.BottomNavScreen.BottomItems

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface
    ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(imageVector = item.icon, contentDescription = null) },
                label = { Text(text = item.title, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
