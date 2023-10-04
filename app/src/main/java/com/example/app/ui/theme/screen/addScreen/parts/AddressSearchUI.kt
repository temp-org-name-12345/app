package com.example.app.ui.theme.screen.addScreen.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.model.Location
import com.example.app.model.RoadAddress
import com.example.app.ui.theme.CustomColor
import com.example.app.ui.theme.common.ToNextOrSubmitButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun AddressSearchUI(
    searchInfo: Location?,
    searchInput: String,
    onSearchInputChanged: (String) -> Unit,
    onLocationSearch: (String) -> Unit,
    onAddressSelected: (RoadAddress?) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    onNextInput: () -> Unit,
    toastWarningMessage: (String) -> Unit
) {
    var searchBarEnable by rememberSaveable { mutableStateOf(true) }
    var lazyColumnAlpha by rememberSaveable { mutableFloatStateOf(1f) }
    var searchTextAlpha by rememberSaveable { mutableFloatStateOf(0f) }
    var selectedAddress by rememberSaveable { mutableStateOf<RoadAddress?>(null) }
    var iconColorDecider by rememberSaveable { mutableStateOf(false) }

    val onAddressSelectedAndInvisible = { addr: RoadAddress? ->
        onAddressSelected(addr)
        lazyColumnAlpha = 0f
        searchTextAlpha = 0f
        selectedAddress = addr
        searchBarEnable = false
        iconColorDecider = true
    }

    val onSearchResultAndTextVisible = { ip: String ->
        val input = ip.trim()

        if (input.isEmpty()) {
            toastWarningMessage("주소를 검색해주세요!")
        }

        else {
            if (input == "비루개") {
                onLocationSearch("경기도 남양주시 별내면 용암비루개길 219-88")
            }

            else {
                onLocationSearch(input)
            }

            searchTextAlpha = 1f
            lazyColumnAlpha = 1f
            selectedAddress = null
            keyboardController?.hide()
        }
    }

    Text(
        text = "주소를 검색해주세요",
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp,
        color = Color.DarkGray
    )

    OutlinedTextField(
        value = if (searchBarEnable) searchInput else selectedAddress?.fullAddress ?: "",

        onValueChange = onSearchInputChanged,

        enabled = searchBarEnable,

        label = { Text("Search") },

        placeholder = { Text(text = "주소를 검색하세요") },

        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint =
                    if (iconColorDecider) CustomColor.ButtonBackgroundColor
                    else Color.DarkGray
            ) },

        trailingIcon = {
            IconButton(onClick = { onSearchResultAndTextVisible(searchInput) }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = Color.DarkGray
                )
            } },

        /*
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() }
        ),
        */

        modifier = Modifier.padding(16.dp)
    )

    ToNextOrSubmitButton(
        onButtonHandle = onNextInput,
        isButtonEnabled = !searchBarEnable
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .alpha(searchTextAlpha)
        ) {

            if (searchInfo?.meta?.totalCount == null || searchInfo.meta.totalCount == 0) {
                Text(
                    text = "검색 결과가 없습니다",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }

            else {
                Text(
                    text = "${searchInfo.meta.totalCount}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )

                Text(
                    text = "개의 결과가 검색되었습니다",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .alpha(lazyColumnAlpha)
        ) {
            searchInfo?.let {
                itemsIndexed(it.documents) { idx, item ->
                    if (idx == 0) {
                        Divider(modifier = Modifier.padding(4.dp))
                    }

                    Card(
                        modifier = Modifier
                            .clickable { onAddressSelectedAndInvisible(item.roadAddress) }
                            .padding(24.dp)
                            .background(Color.White)

                    ) {
                        val address = item.roadAddress?.fullAddress

                        Text(
                            text = "$address",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Divider(modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}
