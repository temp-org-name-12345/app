package com.example.app.ui.theme.screen.splashScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.app.model.User
import com.example.app.ui.theme.CustomColor
import com.example.app.ui.theme.common.LoadingIndicator
import com.example.app.viewModel.DefaultAppViewModel
import com.example.app.viewModel.UserViewModel

@Composable
fun SplashScreen(
    defaultAppViewModel: DefaultAppViewModel,
    userViewModel: UserViewModel,
    keyHash: String,
    navToLogin: () -> Unit,
    navToMap: () -> Unit
) {
    LaunchedEffect(Unit) {
        defaultAppViewModel.getAppThumbnailImage()
        userViewModel.getUserByKeyHash(keyHash)
    }

    val thumbnailImage by defaultAppViewModel.thumbnailImage.observeAsState()
    val user by userViewModel.user.observeAsState()

    val onButtonHandle = { usr: User? ->
        if (usr == null) navToLogin()
        else navToMap()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (user != null) {
            Text(
                text = "${user?.nickname}님 오셨네요!",
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
        }

        if (thumbnailImage == null) LoadingIndicator()
        else {
            AsyncImage(
                model = thumbnailImage?.first(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter),

            colors = ButtonDefaults.buttonColors(
                backgroundColor = CustomColor.ButtonBackgroundColor,
                contentColor = Color.White,
                disabledBackgroundColor = CustomColor.ButtonBackgroundColor,
                disabledContentColor = Color.Gray
            ),

            /* border = BorderStroke(2.dp, Color.DarkGray), */

            elevation = ButtonDefaults.elevation(
                defaultElevation = 10.dp
            ),

            shape = RoundedCornerShape(size = 15.dp),

            onClick = { onButtonHandle(user) }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(width = 8.dp))

            Text(
                text = "시작할래요",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}