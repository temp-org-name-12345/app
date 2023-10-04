package com.example.app.ui.theme.screen.addScreen.parts

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.ui.theme.CustomColor
import com.example.app.ui.theme.common.ToNextOrSubmitButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun BuildingInputUI(
    buildingInput: String,
    onBuildingInputChanged: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    onNextInput: () -> Unit,
    toastWarningMessage: (String) -> Unit
) {
    var iconColorDecider by rememberSaveable { mutableStateOf(false) }
    var buttonEnable by rememberSaveable { mutableStateOf(false) }

    val onCheckButtonClick = {

        if (buildingInput.trim().isEmpty()) {
            toastWarningMessage("장소명을 입력해주세요!")
        }

        else {
            keyboardController?.hide()
            iconColorDecider = true
            buttonEnable = true
        }
    }

    Text(
        text = "장소명을 입력해주세요",
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp,
        color = Color.DarkGray
    )

    OutlinedTextField(
        value = buildingInput,

        onValueChange = onBuildingInputChanged,

        label = { Text(text = "Search") },

        /*
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        */

        placeholder = { Text(text = "장소를 검색하세요") },

        leadingIcon = { Icon(imageVector = Icons.Default.Info, contentDescription = null) },

        trailingIcon = {
            IconButton(onClick = onCheckButtonClick) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint =
                        if (iconColorDecider) CustomColor.ButtonBackgroundColor
                        else Color.DarkGray
                )
            } },

        modifier = Modifier.padding(16.dp)
    )

    ToNextOrSubmitButton(
        isButtonEnabled = buttonEnable,
        onButtonHandle = onNextInput
    )
}