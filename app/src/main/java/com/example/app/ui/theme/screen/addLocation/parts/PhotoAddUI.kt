package com.example.app.ui.theme.screen.addLocation.parts

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
internal fun PhotoAddUI(
    NextButton: @Composable () -> Unit
) {
    Text(text = "사진을 추가해주세요")
    NextButton()
}