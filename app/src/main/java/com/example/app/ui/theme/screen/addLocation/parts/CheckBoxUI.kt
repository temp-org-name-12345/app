package com.example.app.ui.theme.screen.addLocation.parts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
internal fun CheckBoxUI(
    isSpecial: Boolean,
    modifier: Modifier = Modifier,
    onIsSpecialChanged: (Boolean) -> Unit,
    NextButton: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .then(modifier)
    ) {
        Text(
            text = "특별했나요?",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp))

        Checkbox(checked = isSpecial, onCheckedChange = onIsSpecialChanged)
    }

    NextButton()
}
