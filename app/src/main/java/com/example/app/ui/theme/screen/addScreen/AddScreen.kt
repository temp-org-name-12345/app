package com.example.app.ui.theme.screen.addScreen

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.app.ui.theme.common.ToNextOrSubmitButton
import com.example.app.ui.theme.screen.addScreen.parts.AddressSearchUI
import com.example.app.ui.theme.screen.addScreen.parts.BuildingInputUI
import com.example.app.ui.theme.screen.addScreen.parts.CheckBoxUI
import com.example.app.ui.theme.screen.addScreen.parts.DateSelectUI
import com.example.app.ui.theme.screen.addScreen.parts.PhotoAddUI
import com.example.app.ui.theme.screen.addScreen.parts.SubmitResultUI
import com.example.app.viewModel.MapViewModel
import com.example.app.viewModel.UserViewModel
import kotlinx.coroutines.launch
import java.util.Calendar


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddScreen(
    mapViewModel: MapViewModel,
    userViewModel: UserViewModel,
    onPostSubmitRoute: () -> Unit
) {
    /* CONTEXT */
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    /* STATE */
    val searchInfo by userViewModel.searchInfo.observeAsState()
    val searchInput by userViewModel.searchInput.observeAsState()
    val dateState by userViewModel.dateState.observeAsState()
    val isSpecial by userViewModel.isSpecial.observeAsState()
    val buildingInput by userViewModel.buildingInput.observeAsState()
    val uiVisible by userViewModel.uiVisible.observeAsState()
    val selectedImageUris by userViewModel.selectedImages.observeAsState()
    val submitButtonEnabled by userViewModel.submitButtonEnabled.observeAsState()

    /* Handler */
    val toastWarningMessage = { message: String -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show() }

    /* DATE_PICKER */
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        userViewModel.onDateSetListener,
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val coroutineScope = rememberCoroutineScope()

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
                            onLocationSearch = userViewModel.onLocationSearch,
                            onAddressSelected = userViewModel.onAddressSelected,
                            keyboardController = keyboardController,
                            onNextInput = userViewModel.onNextInput,
                            toastWarningMessage = toastWarningMessage
                        )
                    }

                    2 -> {
                        BuildingInputUI(
                            buildingInput = buildingInput ?: "",
                            keyboardController = keyboardController,
                            onBuildingInputChanged = userViewModel.onBuildingInputChanged,
                            onNextInput = userViewModel.onNextInput,
                            toastWarningMessage = toastWarningMessage
                        )
                    }

                    3 -> {
                        DateSelectUI(
                            datePickerDialog = datePickerDialog,
                            dateState = dateState,
                            onNextInput = userViewModel.onNextInput
                        )
                    }

                    4 -> {
                        CheckBoxUI(
                            isSpecial = isSpecial ?: false,
                            onIsSpecialChanged = userViewModel.onIsSpecialChanged,
                        ) {
                            ToNextOrSubmitButton(
                                onButtonHandle = userViewModel.onNextInput
                            )
                        }
                    }

                    5 -> {
                        PhotoAddUI(
                            selectedImageUris = selectedImageUris ?: emptyList(),
                            setSelectedImageUris = userViewModel.onImagesSelected,

                        ) {
                            ToNextOrSubmitButton(onButtonHandle = userViewModel.onNextInput)
                        }
                    }

                    6 -> {
                        SubmitResultUI(
                            userViewModel = userViewModel,
                            submitButtonEnabled = submitButtonEnabled ?: true
                        ) {

                            ToNextOrSubmitButton(
                                text = "제출",
                                isButtonEnabled = submitButtonEnabled ?: true
                            ) {
                                coroutineScope.launch {
                                    userViewModel.handleSubmitButtonEnable()
                                    userViewModel.onSubmitButtonClick(context)
                                    onPostSubmitRoute()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}












