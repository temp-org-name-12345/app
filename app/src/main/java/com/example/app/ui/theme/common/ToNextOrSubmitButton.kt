package com.example.app.ui.theme.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.ui.theme.CustomColor

@Composable
fun ToNextOrSubmitButton(
    text: String = "다음",
    isButtonEnabled: Boolean = true,
    onButtonHandle: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),

        enabled = isButtonEnabled,

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

        onClick = { onButtonHandle() }
    ) {
        Icon(
            imageVector =
                if (text == "다음") Icons.Default.Check
                else Icons.Default.Send,
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(width = 8.dp))

        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = if (isButtonEnabled) Color.White else Color.Gray
        )
    }
}