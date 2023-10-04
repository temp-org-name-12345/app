package com.example.app.ui.theme.screen.addScreen.parts

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.app.viewModel.UserViewModel

@Composable
internal fun SubmitResultUI(
    userViewModel: UserViewModel,
    SubmitButton: @Composable () -> Unit
) {
    Row {
        Text(text = "주소명: ")
        Text(text = userViewModel.selectedAddress.value.toString())
    }

    Row {
        Text(text = "방문일시: ")
        Text(text = userViewModel.dateState.value.toString())
    }

    Row {
        Text(text = "Special: ")
        Text(text = userViewModel.isSpecial.value.toString())
    }

    Row {
        Text(text = "사진: ")
        Text(text = userViewModel.selectedImages.value.toString())
    }

    SubmitButton()
}