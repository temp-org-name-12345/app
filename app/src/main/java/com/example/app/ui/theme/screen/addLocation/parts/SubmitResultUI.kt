package com.example.app.ui.theme.screen.addLocation.parts

import android.net.Uri
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.app.model.RoadAddress
import java.time.LocalDateTime

@Composable
internal fun SubmitResultUI(
    selectedAddress: RoadAddress?,
    buildingAddress: String,
    visitDate: LocalDateTime?,
    isSpecial: Boolean?,
    photos: List<Uri?>,
    SubmitButton: @Composable () -> Unit
) {
    Row {
        Text(text = "주소명: ")
        Text(text = selectedAddress?.fullAddress ?: "선택 없음")
    }

    Row {
        Text(text = "위도: ")
        Text(text = selectedAddress?.lat ?: "선택 없음")
    }

    Row {
        Text(text = "경도: ")
        Text(text = selectedAddress?.lng ?: "선택 없음")
    }

    Row {
        Text(text = "장소명: ")
        Text(text = buildingAddress)
    }

    Row {
        Text(text = "방문일시: ")
        Text(text = visitDate?.toString() ?: "선택 없음")
    }

    Row {
        Text(text = "Special: ")
        Text(text = isSpecial?.toString() ?: "false")
    }

    Row {
        Text(text = "사진: ")
        Text(text = photos.toString())
    }

    SubmitButton()
}