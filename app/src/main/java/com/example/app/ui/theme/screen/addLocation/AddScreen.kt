package com.example.app.ui.theme.screen.addLocation

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.app.model.AddLocationReq
import com.example.app.model.User
import com.example.app.ui.theme.common.ToNextOrSubmitButton
import com.example.app.ui.theme.screen.addLocation.parts.AddressSearchUI
import com.example.app.ui.theme.screen.addLocation.parts.BuildingInputUI
import com.example.app.ui.theme.screen.addLocation.parts.CheckBoxUI
import com.example.app.ui.theme.screen.addLocation.parts.DateSelectUI
import com.example.app.ui.theme.screen.addLocation.parts.PhotoAddUI
import com.example.app.ui.theme.screen.addLocation.parts.SubmitResultUI
import com.example.app.viewModel.MapViewModel
import com.example.app.viewModel.UserViewModel
import java.util.Calendar


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddScreen(
    user: User?,
    mapViewModel: MapViewModel,
    userViewModel: UserViewModel
) {
    /* CONTEXT */
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    /* STATE */
    val searchInfo by mapViewModel.searchInfo.observeAsState()
    val searchInput by userViewModel.searchInput.observeAsState()
    val dateState by userViewModel.dateState.observeAsState()
    val isSpecial by userViewModel.isSpecial.observeAsState()
    val selectedAddress by userViewModel.selectedAddress.observeAsState()

    /* HANDLER */
    var uiVisible by rememberSaveable { mutableIntStateOf(1) }
    val onNextInput = { uiVisible += 1 }

    var buildingInput by rememberSaveable { mutableStateOf("") }
    val onBuildingInputChanged = { input: String -> buildingInput = input }

    val toastWarningMessage = { message: String ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /* DATE_PICKER */
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        userViewModel.onDateSetListener,
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    /* SUBMIT */
    val onSubmit = {
        val req = AddLocationReq(
            lat = selectedAddress?.lat?.toDouble(),
            lng = selectedAddress?.lng?.toDouble(),
            addressName = selectedAddress?.fullAddress,
            storeName = buildingInput,
            visitDate = dateState,
            isSpecial = isSpecial,
            userId = user?.id
        )

        userViewModel.addLocationReq(listOf(), req)
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
                        AddressSearchUI(
                            searchInfo = searchInfo,
                            searchInput = searchInput ?: "",
                            onSearchInputChanged = userViewModel.onSearchInputChanged,
                            onLocationSearch = mapViewModel.onLocationSearch,
                            onAddressSelected = userViewModel.onAddressSelected,
                            keyboardController = keyboardController,
                            onNextInput = onNextInput,
                            toastWarningMessage = toastWarningMessage
                        )
                    }

                    2 -> {
                        BuildingInputUI(
                            buildingInput = buildingInput,
                            keyboardController = keyboardController,
                            onBuildingInputChanged = onBuildingInputChanged,
                            onNextInput = onNextInput,
                            toastWarningMessage = toastWarningMessage
                        )
                    }

                    3 -> {
                        DateSelectUI(
                            datePickerDialog = datePickerDialog,
                            dateState = dateState,
                            onNextInput = onNextInput
                        )
                    }

                    4 -> {
                        CheckBoxUI(
                            isSpecial = isSpecial ?: false,
                            onIsSpecialChanged = userViewModel.onIsSpecialChanged,
                        ) {
                            ToNextOrSubmitButton(onButtonHandle = onNextInput)
                        }
                    }

                    5 -> {
                        PhotoAddUI(
                            userViewModel = userViewModel
                        ) {
                            ToNextOrSubmitButton(onButtonHandle = onNextInput)
                        }
                    }

                    6 -> {
                        SubmitResultUI(
                            userViewModel = userViewModel
                        ) {
                            ToNextOrSubmitButton(
                                text = "제출하기",
                                onButtonHandle = onSubmit
                            )
                        }
                    }
                }
            }
        }
    }
}












