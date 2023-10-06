package com.example.app.ui.theme.screen.addScreen.parts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.app.ui.theme.common.LoadingIndicator
import com.example.app.viewModel.UserViewModel

@Composable
internal fun SubmitResultUI(
    userViewModel: UserViewModel,
    submitButtonEnabled: Boolean,
    SubmitButton: @Composable () -> Unit
) {
    Box {
        if (!submitButtonEnabled) {
            Column(modifier = Modifier.align(Alignment.Center)) {
                LoadingIndicator()
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.Center)
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
        }
    }


    SubmitButton()
}