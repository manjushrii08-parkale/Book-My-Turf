package com.example.bookmyturf.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================
// COLORS
// ============================================================

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)

private val OffWhite = Color(0xFFF8F8F5)
private val White = Color(0xFFFFFFFF)

private val Charcoal = Color(0xFF1C1C1C)
private val Gray = Color(0xFF737373)

private val BorderColor = Color(0xFFDADAD4)
private val ErrorRed = Color(0xFFD32F2F)

// ============================================================
// EDIT PROFILE SCREEN
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    currentName: String = "",
    currentEmail: String = "",
    currentPhone: String = "",
    currentDateOfBirth: String = "",
    isSaving: Boolean = false,
    errorMessage: String? = null,
    onBackClick: () -> Unit,
    onSaveClick: (
        name: String,
        phone: String,
        dateOfBirth: String
    ) -> Unit
) {

    // =========================================================
    // FORM STATE
    // =========================================================

    var name by remember(currentName) {
        mutableStateOf(currentName)
    }

    var phone by remember(currentPhone) {
        mutableStateOf(currentPhone)
    }

    var dateOfBirth by remember(currentDateOfBirth) {
        mutableStateOf(currentDateOfBirth)
    }

    var nameError by remember {
        mutableStateOf<String?>(null)
    }

    var phoneError by remember {
        mutableStateOf<String?>(null)
    }

    // =========================================================
    // VALIDATION
    // =========================================================

    fun validateAndSave() {

        nameError = null
        phoneError = null

        var valid = true

        if (name.trim().isBlank()) {

            nameError = "Name is required"

            valid = false
        }

        if (
            phone.isNotBlank() &&
            phone.length != 10
        ) {

            phoneError =
                "Enter a valid 10-digit mobile number"

            valid = false
        }

        if (valid) {

            onSaveClick(
                name.trim(),
                phone.trim(),
                dateOfBirth.trim()
            )
        }
    }

    // =========================================================
    // SCREEN
    // =========================================================

    Scaffold(

        containerColor = OffWhite,

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text = "Edit Profile",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = Charcoal
                        )

                        Text(
                            text = "Update your personal details",
                            fontSize = 11.sp,
                            color = Gray
                        )
                    }
                },

                navigationIcon = {

                    IconButton(
                        onClick = {

                            if (!isSaving) {
                                onBackClick()
                            }
                        },
                        enabled = !isSaving
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,

                            contentDescription =
                                "Back",

                            tint =
                                Charcoal
                        )
                    }
                },

                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = White,
                        titleContentColor = Charcoal,
                        navigationIconContentColor = Charcoal
                    )
            )
        }

    ) { innerPadding ->

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 18.dp,
                        vertical = 10.dp
                    )
        ) {

            // =================================================
            // PROFILE ICON
            // =================================================

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(20.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor = White
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
            ) {

                Column(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Box(

                        modifier =
                            Modifier
                                .size(68.dp)
                                .background(
                                    LightGreen.copy(
                                        alpha = 0.14f
                                    ),
                                    RoundedCornerShape(18.dp)
                                ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Person,

                            contentDescription =
                                "Profile",

                            modifier =
                                Modifier.size(34.dp),

                            tint =
                                ForestGreen
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    Text(
                        text =
                            "Personal Information",

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            Charcoal
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "Keep your profile information up to date.",

                        fontSize =
                            11.sp,

                        color =
                            Gray
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            // =================================================
            // NAME
            // =================================================

            EditProfileField(
                icon = Icons.Default.Person,
                label = "Full Name",
                value = name,
                placeholder = "Enter your full name",
                onValueChange = {

                    name = it

                    if (it.isNotBlank()) {
                        nameError = null
                    }
                },
                enabled = !isSaving,
                isError = nameError != null,
                errorMessage = nameError
            )

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            // =================================================
            // EMAIL
            // =================================================

            EditProfileField(
                icon = Icons.Default.Email,
                label = "Email",
                value = currentEmail,
                placeholder = "Email",
                onValueChange = {},
                enabled = false
            )

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            // =================================================
            // PHONE
            // =================================================

            EditProfileField(
                icon = Icons.Default.Phone,
                label = "Mobile Number",
                value = phone,
                placeholder = "Enter 10-digit mobile number",
                onValueChange = {

                    if (
                        it.length <= 10 &&
                        it.all { char ->
                            char.isDigit()
                        }
                    ) {

                        phone = it
                    }

                    if (
                        it.isBlank() ||
                        it.length == 10
                    ) {

                        phoneError = null
                    }
                },
                enabled = !isSaving,
                isError = phoneError != null,
                errorMessage = phoneError
            )

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            // =================================================
            // DATE OF BIRTH
            // =================================================

            EditProfileField(
                icon =
                    Icons.Default.CalendarMonth,

                label =
                    "Date of Birth",

                value =
                    dateOfBirth,

                placeholder =
                    "YYYY-MM-DD",

                onValueChange = {
                    dateOfBirth = it
                },

                enabled =
                    !isSaving
            )

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            // =================================================
            // API ERROR
            // =================================================

            if (!errorMessage.isNullOrBlank()) {

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(12.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                ErrorRed.copy(alpha = 0.08f)
                        )
                ) {

                    Text(
                        text =
                            errorMessage,

                        modifier =
                            Modifier.padding(12.dp),

                        fontSize =
                            12.sp,

                        color =
                            ErrorRed,

                        fontWeight =
                            FontWeight.Medium
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )
            }

            // =================================================
            // SAVE BUTTON
            // =================================================

            Button(

                onClick =
                    ::validateAndSave,

                enabled =
                    !isSaving,

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(14.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            DarkGreen,

                        disabledContainerColor =
                            DarkGreen.copy(
                                alpha = 0.6f
                            )
                    )
            ) {

                if (isSaving) {

                    CircularProgressIndicator(

                        modifier =
                            Modifier.size(18.dp),

                        strokeWidth =
                            2.dp,

                        color =
                            White
                    )

                    Spacer(
                        modifier =
                            Modifier.width(8.dp)
                    )

                    Text(
                        text =
                            "Saving...",

                        fontSize =
                            15.sp,

                        fontWeight =
                            FontWeight.Bold,

                        modifier =
                            Modifier.padding(
                                vertical = 4.dp
                            )
                    )

                } else {

                    Icon(
                        imageVector =
                            Icons.Default.Save,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(19.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.width(8.dp)
                    )

                    Text(
                        text =
                            "Save Changes",

                        fontSize =
                            15.sp,

                        fontWeight =
                            FontWeight.Bold,

                        modifier =
                            Modifier.padding(
                                vertical = 4.dp
                            )
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            Text(
                text =
                    "Your email address cannot be changed here.",

                modifier =
                    Modifier.fillMaxWidth(),

                fontSize =
                    10.sp,

                color =
                    Gray
            )

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )
        }
    }
}

// ============================================================
// EDIT PROFILE FIELD
// ============================================================

@Composable
private fun EditProfileField(
    icon: ImageVector,
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null
) {

    Column(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Icon(
                imageVector =
                    icon,

                contentDescription =
                    null,

                modifier =
                    Modifier.size(17.dp),

                tint =
                    ForestGreen
            )

            Spacer(
                modifier =
                    Modifier.width(7.dp)
            )

            Text(
                text =
                    label,

                fontSize =
                    13.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Charcoal
            )
        }

        Spacer(
            modifier =
                Modifier.height(7.dp)
        )

        OutlinedTextField(

            value =
                value,

            onValueChange =
                onValueChange,

            modifier =
                Modifier.fillMaxWidth(),

            enabled =
                enabled,

            singleLine =
                true,

            placeholder = {

                Text(
                    text =
                        placeholder,

                    fontSize =
                        13.sp,

                    color =
                        Gray
                )
            },

            isError =
                isError,

            shape =
                RoundedCornerShape(14.dp),

            colors =
                OutlinedTextFieldDefaults.colors(

                    focusedBorderColor =
                        ForestGreen,

                    unfocusedBorderColor =
                        BorderColor,

                    disabledBorderColor =
                        BorderColor,

                    focusedTextColor =
                        Charcoal,

                    unfocusedTextColor =
                        Charcoal,

                    disabledTextColor =
                        Gray,

                    cursorColor =
                        ForestGreen
                )
        )

        if (isError && errorMessage != null) {

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(
                text =
                    errorMessage,

                fontSize =
                    10.sp,

                color =
                    ErrorRed
            )
        }
    }
}

