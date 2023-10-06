package com.example.app.util

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
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