package com.example.app.ui.theme.screen.addScreen.parts

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
internal fun PhotoAddUI(
    selectedImageUris: List<Uri>,
    setSelectedImageUris: (List<Uri>) -> Unit,
    NextButton: @Composable () -> Unit
) {
    Text(text = "사진을 추가해주세요")

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> setSelectedImageUris(uris) }
    )

    Button(onClick = {
        multiplePhotoPickerLauncher.launch(
            PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )
    }) {
        Text("사진 여러 장 선택")
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        )
    ) {
        items(selectedImageUris) { uri ->
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                elevation = 8.dp
            ) {
                AsyncImage(
                    model = uri,
                    contentDescription = null
                )
            }
        }
    }

    NextButton()
}