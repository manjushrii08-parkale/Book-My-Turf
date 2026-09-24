package com.example.bookmyturf.screens.admin

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// =============================================================
// PREMIUM BOOKMYTURF COLORS
// =============================================================

private val Background = Color(0xFF020907)
private val SurfaceDark = Color(0xFF06110D)
private val SurfaceElevated = Color(0xFF091711)
private val SurfaceHighlight = Color(0xFF0D2017)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)
private val MutedText = Color(0xFF687871)

private val Border = Color(0xFF183027)

private val ErrorRed = Color(0xFFFF6B6B)


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
    // VALIDATION
    // =========================================================

    var validationError by remember {
        mutableStateOf<String?>(null)
    }


    // =========================================================
    // SNACKBAR
    // =========================================================

    val snackbarHostState = remember {
        SnackbarHostState()
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
    // SHOW SERVER ERROR
    // =========================================================

    LaunchedEffect(errorMessage) {

        if (!errorMessage.isNullOrBlank()) {

            snackbarHostState.showSnackbar(
                message = errorMessage
            )
        }
    }


    // =========================================================
    // MAIN UI
    // =========================================================

    Scaffold(

        containerColor = Background,

        topBar = {

            PremiumEditProfileTopBar(
                onBackClick = onBackClick,
                enabled = !isSaving
            )
        },

        snackbarHost = {

            SnackbarHost(
                hostState = snackbarHostState
            ) { data ->

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = SurfaceElevated,
                    border = BorderStroke(
                        1.dp,
                        ErrorRed.copy(alpha = 0.35f)
                    )
                ) {

                    Text(
                        text = data.visuals.message,
                        color = PrimaryText,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 13.dp
                        )
                    )
                }
            }
        }

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
                .imePadding()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 18.dp,
                    vertical = 22.dp
                ),

            verticalArrangement = Arrangement.Top
        ) {

            // =================================================
            // HEADER
            // =================================================

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Edit Profile",
                    color = PrimaryText,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Update your personal details",
                    color = SecondaryText,
                    fontSize = 14.sp
                )
            }


            Spacer(
                modifier = Modifier.height(24.dp)
            )


            // =================================================
            // PROFILE INFORMATION CARD
            // =================================================

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = SurfaceDark,
                border = BorderStroke(
                    1.dp,
                    Border
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    // -----------------------------------------
                    // SECTION TITLE
                    // -----------------------------------------

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Surface(
                            modifier = Modifier.size(38.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = SurfaceHighlight
                        ) {

                            Box(
                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Default.Person,

                                    contentDescription = null,

                                    tint = LightGreen,

                                    modifier =
                                        Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier.size(12.dp)
                        )

                        Column {

                            Text(
                                text = "Personal Information",
                                color = PrimaryText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(
                                modifier = Modifier.height(2.dp)
                            )

                            Text(
                                text = "Keep your account details up to date",
                                color = MutedText,
                                fontSize = 12.sp
                            )
                        }
                    }


                    Spacer(
                        modifier = Modifier.height(22.dp)
                    )


                    // =================================================
                    // FULL NAME
                    // =================================================

                    PremiumProfileTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            validationError = null
                        },
                        enabled = !isSaving,
                        label = "Full Name",
                        placeholder = "Enter your full name",
                        icon = {
                            Icon(
                                imageVector =
                                    Icons.Default.Person,
                                contentDescription = "Name"
                            )
                        }
                    )


                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )


                    // =================================================
                    // EMAIL
                    // =================================================

                    PremiumProfileTextField(
                        value = currentEmail,
                        onValueChange = {},
                        enabled = false,
                        readOnly = true,
                        label = "Email",
                        placeholder = "",
                        icon = {
                            Icon(
                                imageVector =
                                    Icons.Default.Email,
                                contentDescription = "Email"
                            )
                        }
                    )


                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )


                    // =================================================
                    // PHONE
                    // =================================================

                    PremiumProfileTextField(
                        value = phone,
                        onValueChange = { value ->

                            val digitsOnly =
                                value
                                    .filter {
                                        it.isDigit()
                                    }
                                    .take(10)

                            phone = digitsOnly
                            validationError = null
                        },
                        enabled = !isSaving,
                        label = "Mobile Number",
                        placeholder = "Enter 10-digit mobile number",
                        icon = {
                            Icon(
                                imageVector =
                                    Icons.Default.Phone,
                                contentDescription = "Phone"
                            )
                        },
                        keyboardType = KeyboardType.Phone
                    )


                    // =================================================
                    // VALIDATION ERROR
                    // =================================================

                    if (
                        !validationError.isNullOrBlank()
                    ) {

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = ErrorRed.copy(alpha = 0.07f),
                            border = BorderStroke(
                                1.dp,
                                ErrorRed.copy(alpha = 0.20f)
                            )
                        ) {

                            Text(
                                text = validationError ?: "",
                                color = ErrorRed,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(
                                    horizontal = 14.dp,
                                    vertical = 11.dp
                                )
                            )
                        }
                    }
                }
            }


            Spacer(
                modifier = Modifier.height(24.dp)
            )


            // =================================================
            // ACCOUNT NOTE
            // =================================================

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = SurfaceElevated
            ) {

                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Surface(
                        modifier = Modifier.size(8.dp),
                        shape = RoundedCornerShape(50),
                        color = PrimaryGreen
                    ) {}

                    Spacer(
                        modifier = Modifier.size(12.dp)
                    )

                    Text(
                        text = "Your email address cannot be changed from this screen.",
                        color = SecondaryText,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(24.dp)
            )


            HorizontalDivider(
                color = Border
            )


            Spacer(
                modifier = Modifier.height(22.dp)
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
                    // CLEAR ERROR
                    // -----------------------------------------

                    validationError = null


                    // -----------------------------------------
                    // SAVE
                    // -----------------------------------------

                    onSaveClick(
                        trimmedName,
                        trimmedPhone
                    )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                enabled = !isSaving,

                shape = RoundedCornerShape(16.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Background,
                    disabledContainerColor =
                        PrimaryGreen.copy(alpha = 0.35f),
                    disabledContentColor =
                        Background.copy(alpha = 0.55f)
                )
            ) {

                if (isSaving) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Background,
                        strokeWidth = 2.5.dp
                    )

                } else {

                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(19.dp)
                    )

                    Spacer(
                        modifier = Modifier.size(9.dp)
                    )

                    Text(
                        text = "Save Changes",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(28.dp)
            )


            // =================================================
            // FOOTER
            // =================================================

            Text(
                text = "BookMyTurf • Admin Profile",
                modifier = Modifier.fillMaxWidth(),
                color = MutedText,
                fontSize = 11.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )


            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
    }
}


// =============================================================
// PREMIUM TOP BAR
// =============================================================

@Composable
private fun PremiumEditProfileTopBar(
    onBackClick: () -> Unit,
    enabled: Boolean
) {

    Surface(
        color = Background,
        shadowElevation = 0.dp
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF020907),
                            Color(0xFF071810),
                            Color(0xFF020907)
                        )
                    )
                )
        ) {

            Column {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 14.dp,
                            end = 14.dp,
                            top = 36.dp,
                            bottom = 14.dp
                        ),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Surface(
                        modifier = Modifier.size(42.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = SurfaceElevated
                    ) {

                        IconButton(
                            onClick = onBackClick,
                            enabled = enabled
                        ) {

                            Icon(
                                imageVector =
                                    Icons.AutoMirrored.Filled.ArrowBack,

                                contentDescription = "Back",

                                tint = PrimaryText,

                                modifier =
                                    Modifier.size(20.dp)
                            )
                        }
                    }


                    Spacer(
                        modifier = Modifier.size(14.dp)
                    )


                    Text(
                        text = "BookMyTurf",
                        color = PrimaryText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.3).sp
                    )
                }


                // =================================================
                // PREMIUM HORIZONTAL LINE
                // =================================================

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Border,
                                    PrimaryGreen.copy(
                                        alpha = 0.18f
                                    ),
                                    Border,
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
        }
    }
}


// =============================================================
// PREMIUM TEXT FIELD
// =============================================================

@Composable
private fun PremiumProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    label: String,
    placeholder: String,
    icon: @Composable () -> Unit,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {

    OutlinedTextField(

        value = value,

        onValueChange = onValueChange,

        modifier = Modifier.fillMaxWidth(),

        enabled = enabled,

        readOnly = readOnly,

        singleLine = true,

        label = {
            Text(
                text = label
            )
        },

        placeholder = {

            if (placeholder.isNotBlank()) {

                Text(
                    text = placeholder
                )
            }
        },

        leadingIcon = icon,

        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),

        shape = RoundedCornerShape(14.dp),

        colors = OutlinedTextFieldDefaults.colors(

            focusedTextColor =
                PrimaryText,

            unfocusedTextColor =
                PrimaryText,

            disabledTextColor =
                MutedText,

            focusedContainerColor =
                SurfaceElevated,

            unfocusedContainerColor =
                SurfaceElevated,

            disabledContainerColor =
                SurfaceHighlight,

            focusedBorderColor =
                PrimaryGreen,

            unfocusedBorderColor =
                Border,

            disabledBorderColor =
                Border,

            focusedLabelColor =
                LightGreen,

            unfocusedLabelColor =
                SecondaryText,

            disabledLabelColor =
                MutedText,

            focusedLeadingIconColor =
                PrimaryGreen,

            unfocusedLeadingIconColor =
                SecondaryText,

            disabledLeadingIconColor =
                MutedText,

            focusedPlaceholderColor =
                MutedText,

            unfocusedPlaceholderColor =
                MutedText
        )
    )
}