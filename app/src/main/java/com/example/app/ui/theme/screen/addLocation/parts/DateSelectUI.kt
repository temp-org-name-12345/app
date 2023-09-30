package com.example.app.ui.theme.screen.addLocation.parts

import android.app.DatePickerDialog
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.app.ui.theme.common.ToNextOrSubmitButton
import java.time.LocalDateTime

@Composable
internal fun DateSelectUI(
    datePickerDialog: DatePickerDialog,
    dateState: LocalDateTime?,
    onNextInput: () -> Unit
) {
    Text(
        text = "날짜를 선택해주세요",
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp,
        color = Color.DarkGray
    )

    IconButton(onClick = { datePickerDialog.show() }) {
        Icon(imageVector = Icons.Filled.DateRange, contentDescription = null)
    }

    dateState?.let {
        Text(
            text = dateState.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }

    ToNextOrSubmitButton(
        enabled = dateState != null,
        onButtonHandle = onNextInput
    )
}
