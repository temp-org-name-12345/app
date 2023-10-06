package com.example.app.ui.theme.screen.addScreen.parts

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
import java.time.LocalDate

@Composable
internal fun DateSelectUI(
    datePickerDialog: DatePickerDialog,
    dateState: LocalDate?,
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
        isButtonEnabled = dateState != null,
        onButtonHandle = onNextInput
    )
}
