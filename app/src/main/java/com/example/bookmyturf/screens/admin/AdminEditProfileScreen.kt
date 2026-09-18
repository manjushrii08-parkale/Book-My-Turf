package com.example.bookmyturf.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminOffWhite


// =============================================================
// ADMIN EDIT PROFILE SCREEN
// =============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEditProfileScreen(
    currentName: String = "",
    currentEmail: String = "",
    currentPhone: String = "",
    isSaving: Boolean = false,
    errorMessage: String? = null,
    onBackClick: () -> Unit,
    onSaveClick: (
        name: String,
        phone: String
    ) -> Unit
) {

    // =========================================================
    // LOCAL FORM STATE
    // =========================================================

    var name by remember(currentName) {
        mutableStateOf(currentName)
    }

    var phone by remember(currentPhone) {
        mutableStateOf(currentPhone)
    }

    // =========================================================
    // LOCAL VALIDATION ERROR
    // =========================================================

    var validationError by remember {
        mutableStateOf<String?>(null)
    }


    // =========================================================
    // UPDATE FORM WHEN SERVER DATA CHANGES
    // =========================================================

    LaunchedEffect(
        currentName,
        currentPhone
    ) {

        name = currentName
        phone = currentPhone
    }


    // =========================================================
    // MAIN UI
    // =========================================================

    Scaffold(

        containerColor =
            AdminOffWhite,

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text = "Edit Profile"
                        )

                        Text(
                            text = "Update your personal details",
                            color = AdminGray
                        )
                    }
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBackClick,
                        enabled = !isSaving
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,

                            contentDescription =
                                "Back"
                        )
                    }
                },

                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor =
                            AdminOffWhite,

                        titleContentColor =
                            AdminDarkGreen,

                        navigationIconContentColor =
                            AdminDarkGreen
                    )
            )
        }

    ) { paddingValues ->

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .imePadding()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 20.dp,
                        vertical = 20.dp
                    ),

            verticalArrangement =
                Arrangement.Top
        ) {

            // =================================================
            // FULL NAME
            // =================================================

            OutlinedTextField(

                value =
                    name,

                onValueChange = {

                    name = it
                    validationError = null
                },

                modifier =
                    Modifier.fillMaxWidth(),

                enabled =
                    !isSaving,

                singleLine = true,

                label = {
                    Text("Full Name")
                },

                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Default.Person,

                        contentDescription =
                            "Name"
                    )
                },

                colors =
                    TextFieldDefaults.colors()
            )


            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            // =================================================
            // EMAIL
            // =================================================

            OutlinedTextField(

                value =
                    currentEmail,

                onValueChange = {},

                modifier =
                    Modifier.fillMaxWidth(),

                enabled = false,

                readOnly = true,

                singleLine = true,

                label = {
                    Text("Email")
                },

                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Default.Email,

                        contentDescription =
                            "Email"
                    )
                },

                colors =
                    TextFieldDefaults.colors(
                        disabledTextColor =
                            AdminGray,

                        disabledLabelColor =
                            AdminGray,

                        disabledLeadingIconColor =
                            AdminGray
                    )
            )


            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            // =================================================
            // MOBILE NUMBER
            // =================================================

            OutlinedTextField(

                value =
                    phone,

                onValueChange = { value ->

                    val digitsOnly =
                        value
                            .filter {
                                it.isDigit()
                            }
                            .take(10)

                    phone =
                        digitsOnly

                    validationError = null
                },

                modifier =
                    Modifier.fillMaxWidth(),

                enabled =
                    !isSaving,

                singleLine = true,

                label = {
                    Text("Mobile Number")
                },

                placeholder = {
                    Text("Enter 10-digit mobile number")
                },

                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Default.Phone,

                        contentDescription =
                            "Phone"
                    )
                },

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Phone
                    ),

                colors =
                    TextFieldDefaults.colors()
            )


            // =================================================
            // VALIDATION ERROR
            // =================================================

            if (
                !validationError.isNullOrBlank()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    text =
                        validationError
                            ?: "",

                    color =
                        androidx.compose.material3.MaterialTheme
                            .colorScheme
                            .error
                )
            }


            // =================================================
            // SERVER ERROR
            // =================================================

            if (
                !errorMessage.isNullOrBlank()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                Text(
                    text =
                        errorMessage
                            ?: "",

                    color =
                        androidx.compose.material3.MaterialTheme
                            .colorScheme
                            .error
                )
            }


            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )


            HorizontalDivider()


            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )


            // =================================================
            // SAVE BUTTON
            // =================================================

            Button(

                onClick = {

                    val trimmedName =
                        name.trim()

                    val trimmedPhone =
                        phone.trim()

                    // -----------------------------------------
                    // NAME VALIDATION
                    // -----------------------------------------

                    if (
                        trimmedName.isBlank()
                    ) {

                        validationError =
                            "Please enter your full name."

                        return@Button
                    }

                    if (
                        trimmedName.length < 2
                    ) {

                        validationError =
                            "Name must contain at least 2 characters."

                        return@Button
                    }


                    // -----------------------------------------
                    // PHONE VALIDATION
                    //
                    // Phone is optional.
                    // If entered, it must contain 10 digits.
                    // -----------------------------------------

                    if (
                        trimmedPhone.isNotBlank() &&
                        trimmedPhone.length != 10
                    ) {

                        validationError =
                            "Mobile number must contain exactly 10 digits."

                        return@Button
                    }


                    // -----------------------------------------
                    // CLEAR VALIDATION ERROR
                    // -----------------------------------------

                    validationError =
                        null


                    // -----------------------------------------
                    // SAVE
                    // -----------------------------------------

                    onSaveClick(
                        trimmedName,
                        trimmedPhone
                    )
                },

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(54.dp),

                enabled =
                    !isSaving,

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            AdminDarkGreen
                    )
            ) {

                if (isSaving) {

                    CircularProgressIndicator(
                        modifier =
                            Modifier.height(22.dp),
                        strokeWidth = 2.dp
                    )

                } else {

                    Text(
                        text =
                            "Save Changes"
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )
        }
    }
}
