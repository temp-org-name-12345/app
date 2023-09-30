package com.example.app.ui.theme.screen

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Space
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.model.AddLocationReq
import com.example.app.model.Location
import com.example.app.model.RoadAddress
import com.example.app.model.User
import com.example.app.ui.theme.CustomColor
import com.example.app.viewModel.MapViewModel
import com.example.app.viewModel.UserViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.time.LocalDateTime
import java.util.Calendar



@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AddScreen(
    user: User?,
    mapViewModel: MapViewModel,
    userViewModel: UserViewModel
) {
    /* CONTEXT */
    val context = LocalContext.current

    /* STATE */
    val searchInfo by mapViewModel.searchInfo.observeAsState()
    val searchInput by userViewModel.searchInput.observeAsState()
    val dateState by userViewModel.dateState.observeAsState()
    val isSpecial by userViewModel.isSpecial.observeAsState()
    val selectedAddress by userViewModel.selectedAddress.observeAsState()

    /* HANDLER */
    var uiVisible by rememberSaveable { mutableIntStateOf(1) }
    val onUIHandle = { uiVisible += 1 }

    /* DATE_PICKER */
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        userViewModel.onDateSetListener,
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    /* CAMERA & GALLERY */
    val photos = mutableListOf<Uri?>()
    val onPhotoChanged = { takenPhoto: Uri? -> photos.add(takenPhoto) }
    val imageTypes = arrayOf("image/jpeg", "image/png", "image/bmp", "image/webp")

    val takePhotoFromAlbumIntent =
        Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
            putExtra(Intent.EXTRA_MIME_TYPES, imageTypes)
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        }

    // 갤러리에서 사진 가져오기
    val takePhotoFromAlbumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri -> onPhotoChanged(uri) }
                    ?:
                run { Toast.makeText(context, "사진 불러오기 실패", Toast.LENGTH_SHORT).show() }
            }

            else if (result.resultCode != Activity.RESULT_CANCELED) {
                Toast.makeText(context, "사진 불러오기 실패", Toast.LENGTH_SHORT).show()
            }
        }

    /* SUBMIT */
    val onSubmit = {
        val req = AddLocationReq(
            lat = selectedAddress?.lat?.toDouble(),
            lng = selectedAddress?.lng?.toDouble(),
            name = selectedAddress?.fullAddress,
            visitDate = dateState,
            isSpecial = isSpecial,
            userId = user?.id
        )

        val images = photos.map {
            it?.let {
                val filePath = it.path
                val file = File(filePath)
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

                MultipartBody.Part.createFormData("images", file.name, requestFile)
            }
        }

        userViewModel.addLocationReq(images, req)
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
            ) {
                when (uiVisible) {
                    1 -> {
                        SearchBar(
                            searchInfo = searchInfo,
                            searchInput = searchInput ?: "",
                            onSearchInputChanged = userViewModel.onSearchInputChanged,
                            onLocationSearch = mapViewModel.onLocationSearch,
                            onAddressSelected = userViewModel.onAddressSelected,
                            onUIHandle = onUIHandle
                        )
                    }

                    2 -> {
                        DateSelectUI(
                            datePickerDialog = datePickerDialog,
                            dateState = dateState,
                            onUIHandle = onUIHandle
                        )
                    }

                    3 -> {

                    }
                }
            }

            /*
            Column(
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Text(text = "특별했나요?")

                CheckBoxLayout(
                    isSpecial = isSpecial ?: false,
                    onIsSpecialChanged = userViewModel.onIsSpecialChanged
                )
            }

            Column(
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                PhotoAddLayout()
            }

            Column(
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Button(onClick = { onSubmit() }) {
                    Text(text = "제출하기")
                }
            }
             */
        }
    }
}

@Composable
private fun SearchBar(
    searchInfo: Location?,
    searchInput: String,
    onSearchInputChanged: (String) -> Unit,
    onLocationSearch: (String) -> Unit,
    onAddressSelected: (RoadAddress?) -> Unit,
    onUIHandle: () -> Unit
) {
    var searchBarEnable by rememberSaveable { mutableStateOf(true) }
    var lazyColumnAlpha by rememberSaveable { mutableFloatStateOf(1f) }
    var searchTextAlpha by rememberSaveable { mutableFloatStateOf(0f) }
    var selectedAddress by rememberSaveable { mutableStateOf<RoadAddress?>(null) }

    val onAddressSelectedAndInvisible = { addr: RoadAddress? ->
        onAddressSelected(addr)
        lazyColumnAlpha = 0f
        searchTextAlpha = 0f
        selectedAddress = addr
        searchBarEnable = false
    }

    val onSearchResultAndTextVisible = { searchInput: String ->
        onLocationSearch(searchInput)
        searchTextAlpha = 1f
        lazyColumnAlpha = 1f
        selectedAddress = null
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "주소를 선택해주세요",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            color = Color.DarkGray
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = if (searchBarEnable) searchInput else selectedAddress?.fullAddress ?: "",
                onValueChange = onSearchInputChanged,
                enabled = searchBarEnable,
                label = { Text("Search") },
                placeholder = { Text(text = "주소를 검색하세요") },
                leadingIcon = { Icon(imageVector = Icons.Default.Info, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { onSearchResultAndTextVisible(searchInput) }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                    } },
                modifier = Modifier.padding(16.dp)
            )
        }

        if (!searchBarEnable) {
            Button(onClick = { onUIHandle() }) {
                Text(text = "다음")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .alpha(searchTextAlpha)
        ) {
            Text(
                text = "${searchInfo?.meta?.totalCount ?: 0}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = "개의 결과가 검색되었습니다",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
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

@Composable
fun DateSelectUI(
    datePickerDialog: DatePickerDialog,
    dateState: LocalDateTime?,
    onUIHandle: () -> Unit
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

    Button(onClick = { onUIHandle() }) {
        Text(text = "다음")
    }
}

@Composable
private fun CheckBoxLayout(
    isSpecial: Boolean,
    modifier: Modifier = Modifier,
    onIsSpecialChanged: (Boolean) -> Unit,
    onUIHandle: () -> Unit
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

    Button(onClick = { onUIHandle() }) {
        Text(text = "다음")
    }
}

@Composable
private fun PhotoAddLayout() {
    Text(text = "사진을 추가해주세요")


}

@Suppress("DEPRECATION", "NewApi")
private fun Uri.parseBitmap(context: Context): Bitmap {
    return when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { // 28
        true -> {
            val source = ImageDecoder.createSource(context.contentResolver, this)
            ImageDecoder.decodeBitmap(source)
        }
        else -> {
            MediaStore.Images.Media.getBitmap(context.contentResolver, this)
        }
    }
}