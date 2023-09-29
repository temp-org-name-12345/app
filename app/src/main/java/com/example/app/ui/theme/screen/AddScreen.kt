package com.example.app.ui.theme.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.app.R
import com.example.app.model.Location
import com.example.app.viewModel.MapViewModel

@Composable
fun AddScreen(mapViewModel: MapViewModel) {
    val key = stringResource(id = R.string.KAKAO_REST_API_KEY)
    val searchInfo by mapViewModel.searchInfo.observeAsState()

    val onLocationSearch = { query: String -> mapViewModel.searchLocationInfo(key, query) }

    Column {
        SimpleOutlinedTextFieldSample(onLocationSearch)

        Output(searchInfo)
    }
}

@Composable
fun SimpleOutlinedTextFieldSample(onLocationSearch: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Label") }
    )

    Button(onClick = { onLocationSearch(text) }) {

    }
}

@Composable
fun Output(searchInfo: Location?) {
    val meta = searchInfo?.meta
    val document = searchInfo?.documents

    Column {
        Text(text = meta.toString())
    }

    Divider(modifier = Modifier.padding(16.dp))

    LazyColumn {
        document?.let {
            itemsIndexed(it) { _, item ->
                Text(text = item.addressName)
                Text(text = item.lat)
                Text(text = item.lng)
                Text(text = item.addressType)
                
                item.roadAddress?.let { 
                    Text(text = it.fullAddress)
                }

                Spacer(modifier = Modifier.padding(4.dp))
            }
        }
    }
}