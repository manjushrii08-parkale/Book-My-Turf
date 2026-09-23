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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================
// PREMIUM DARK THEME
// ============================================================

private val Background = Color(0xFF020907)
private val SurfaceDark = Color(0xFF071410)
private val SurfaceElevated = Color(0xFF0B1C15)
private val SurfaceHighlight = Color(0xFF10271D)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)
private val BrightGreen = Color(0xFFC5F58B)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)
private val MutedText = Color(0xFF687871)

private val Border = Color(0xFF1A3027)
private val ErrorRed = Color(0xFFFF6B6B)

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
            phoneError = "Enter a valid 10-digit mobile number"
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
        containerColor = Background,

        topBar = {

            Column {

                TopAppBar(

                    title = {

                        Column {

                            Text(
                                text = "Edit Profile",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryText
                            )

                            Text(
                                text = "Update your personal details",
                                fontSize = 11.sp,
                                color = SecondaryText
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
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = PrimaryText
                            )
                        }
                    },

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Background,
                        titleContentColor = PrimaryText,
                        navigationIconContentColor = PrimaryText
                    )
                )

                HorizontalDivider(
                    color = Border,
                    thickness = 1.dp
                )
            }
        }

    ) { innerPadding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 20.dp,
                    vertical = 20.dp
                )
                .navigationBarsPadding()
        ) {

            // =================================================
            // PROFILE HERO
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(
                            PrimaryGreen.copy(alpha = 0.14f)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = LightGreen
                    )
                }

                Spacer(
                    modifier = Modifier.width(14.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Personal information",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "Keep your account details up to date.",
                        fontSize = 12.sp,
                        color = SecondaryText
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // =================================================
            // NAME
            // =================================================

            PremiumEditField(
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
                modifier = Modifier.height(18.dp)
            )

            // =================================================
            // EMAIL
            // =================================================

            PremiumEditField(
                icon = Icons.Default.Email,
                label = "Email Address",
                value = currentEmail,
                placeholder = "Email",
                onValueChange = {},
                enabled = false
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // =================================================
            // PHONE
            // =================================================

            PremiumEditField(
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
                modifier = Modifier.height(18.dp)
            )

            // =================================================
            // DATE OF BIRTH
            // =================================================

            PremiumEditField(
                icon = Icons.Default.CalendarMonth,
                label = "Date of Birth",
                value = dateOfBirth,
                placeholder = "YYYY-MM-DD",
                onValueChange = {
                    dateOfBirth = it
                },
                enabled = !isSaving
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // =================================================
            // API ERROR
            // =================================================

            if (!errorMessage.isNullOrBlank()) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(12.dp)
                        )
                        .background(
                            ErrorRed.copy(alpha = 0.08f)
                        )
                        .padding(13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(ErrorRed)
                    )

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    Text(
                        text = errorMessage,
                        fontSize = 12.sp,
                        color = ErrorRed,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )
            }

            // =================================================
            // SAVE BUTTON
            // =================================================

            Button(

                onClick = ::validateAndSave,

                enabled = !isSaving,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),

                shape = RoundedCornerShape(15.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Background,
                    disabledContainerColor =
                        PrimaryGreen.copy(alpha = 0.45f),
                    disabledContentColor =
                        Background.copy(alpha = 0.65f)
                ),

                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp
                )
            ) {

                if (isSaving) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(19.dp),
                        strokeWidth = 2.dp,
                        color = Background
                    )

                    Spacer(
                        modifier = Modifier.width(9.dp)
                    )

                    Text(
                        text = "Saving...",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                } else {

                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(19.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(9.dp)
                    )

                    Text(
                        text = "Save Changes",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // =================================================
            // EMAIL NOTE
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Your email address cannot be changed here.",
                    fontSize = 10.sp,
                    color = MutedText
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }
    }
}

// ============================================================
// PREMIUM EDIT PROFILE FIELD
// ============================================================

@Composable
private fun PremiumEditField(
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
        modifier = Modifier.fillMaxWidth()
    ) {

        // -----------------------------------------------------
        // LABEL
        // -----------------------------------------------------

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (enabled) {
                    LightGreen
                } else {
                    MutedText
                }
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (enabled) {
                    SecondaryText
                } else {
                    MutedText
                }
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        // -----------------------------------------------------
        // FIELD
        // -----------------------------------------------------

        OutlinedTextField(

            value = value,

            onValueChange = onValueChange,

            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (enabled) {
                        SurfaceElevated
                    } else {
                        SurfaceDark
                    },
                    shape = RoundedCornerShape(14.dp)
                ),

            enabled = enabled,

            singleLine = true,

            placeholder = {

                Text(
                    text = placeholder,
                    fontSize = 13.sp,
                    color = MutedText
                )
            },

            isError = isError,

            shape = RoundedCornerShape(14.dp),

            colors = OutlinedTextFieldDefaults.colors(

                focusedContainerColor = SurfaceElevated,
                unfocusedContainerColor = SurfaceElevated,
                disabledContainerColor = SurfaceDark,

                focusedBorderColor = PrimaryGreen,
                unfocusedBorderColor = Border,
                disabledBorderColor = Border,

                errorBorderColor = ErrorRed,

                focusedTextColor = PrimaryText,
                unfocusedTextColor = PrimaryText,
                disabledTextColor = MutedText,

                focusedPlaceholderColor = MutedText,
                unfocusedPlaceholderColor = MutedText,
                disabledPlaceholderColor = MutedText,

                cursorColor = PrimaryGreen,

                errorTextColor = ErrorRed
            )
        )

        // -----------------------------------------------------
        // FIELD ERROR
        // -----------------------------------------------------

        if (isError && errorMessage != null) {

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = errorMessage,
                fontSize = 10.sp,
                color = ErrorRed
            )
        }
    }
}
