package com.example.bookmyturf.screens.auth

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.R
import com.example.bookmyturf.ui.theme.AdminDarkCharcoal
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminForestGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminLightGreen
import com.example.bookmyturf.ui.theme.AdminOffWhite
import com.example.bookmyturf.ui.theme.AdminWhite
import com.example.bookmyturf.viewmodel.AuthViewModel

@Composable
fun PhoneLoginScreen(
    role: String,
    onOtpSent: (String) -> Unit,
    viewModel: AuthViewModel = viewModel()
) {

    // =========================================================
    // EMAIL STATE
    // =========================================================

    var email by rememberSaveable {
        mutableStateOf("")
    }

    // =========================================================
    // UI STATE
    // =========================================================

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    // =========================================================
    // OTP SENT
    // =========================================================

    LaunchedEffect(uiState.otpSent) {
        if (uiState.otpSent) {
            onOtpSent(email.trim())
        }
    }

    // =========================================================
    // ROLE DETAILS
    // =========================================================

    val roleTitle = when (role) {
        "ADMIN" -> "TURF OWNER"
        "SUPER_ADMIN" -> "SUPER ADMIN"
        else -> "USER"
    }

    val mainTitle = when (role) {
        "ADMIN" -> "Welcome back, Turf Owner"
        "SUPER_ADMIN" -> "Welcome back, Super Admin"
        else -> "Welcome back!"
    }

    val subtitle = when (role) {
        "ADMIN" ->
            "Manage your turf, slots and bookings."

        "SUPER_ADMIN" ->
            "Manage users, turf owners and platform activity."

        else ->
            "Book your favourite turf in seconds."
    }

    // =========================================================
    // SCREEN
    // =========================================================

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // =====================================================
        // BACKGROUND IMAGE
        // =====================================================

        Image(
            painter = painterResource(
                id = R.drawable.bg_turf
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // =====================================================
        // BACKGROUND OVERLAY
        // =====================================================

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AdminDarkGreen.copy(alpha = 0.10f),
                            Color.Black.copy(alpha = 0.25f),
                            Color.Black.copy(alpha = 0.72f)
                        )
                    )
                )
        )

        // =====================================================
        // TOP HERO SECTION
        // =====================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 55.dp,
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 355.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            // =================================================
            // LOGO
            // No white circle
            // =================================================

            Image(
                painter = painterResource(
                    id = R.drawable.logo
                ),

                contentDescription =
                    "Book My Turf logo",

                modifier =
                    Modifier.size(175.dp),

                contentScale =
                    ContentScale.Fit
            )

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )

            // =================================================
            // SMALL GREEN BRAND LINE
            // =================================================

            Box(
                modifier = Modifier
                    .width(45.dp)
                    .height(4.dp)
                    .background(
                        color = AdminLightGreen,
                        shape =
                            RoundedCornerShape(50.dp)
                    )
            )
        }

        // =====================================================
        // LOGIN CARD
        // =====================================================

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),

            shape =
                RoundedCornerShape(
                    topStart = 30.dp,
                    topEnd = 30.dp
                ),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        AdminOffWhite
                ),

            elevation =
                CardDefaults.cardElevation(
                    defaultElevation = 14.dp
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 25.dp
                    )
            ) {

                // =================================================
                // TOP HANDLE
                // =================================================

                Box(
                    modifier =
                        Modifier.fillMaxWidth(),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Box(
                        modifier = Modifier
                            .width(42.dp)
                            .height(4.dp)
                            .background(
                                color =
                                    Color(0xFFD3D8D3),
                                shape =
                                    RoundedCornerShape(50.dp)
                            )
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(18.dp)
                )

                // =================================================
                // ROLE BADGE
                // =================================================

                Surface(
                    shape =
                        RoundedCornerShape(50.dp),

                    color =
                        AdminLightGreen.copy(
                            alpha = 0.18f
                        )
                ) {

                    Row(
                        modifier =
                            Modifier.padding(
                                horizontal = 13.dp,
                                vertical = 7.dp
                            ),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.VerifiedUser,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(15.dp),

                            tint =
                                AdminForestGreen
                        )

                        Spacer(
                            modifier =
                                Modifier.width(6.dp)
                        )

                        Text(
                            text =
                                roleTitle,

                            color =
                                AdminForestGreen,

                            fontSize =
                                10.sp,

                            fontWeight =
                                FontWeight.Bold,

                            letterSpacing =
                                1.sp
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(13.dp)
                )

                // =================================================
                // TITLE
                // =================================================

                Text(
                    text =
                        mainTitle,

                    color =
                        AdminDarkCharcoal,

                    fontSize =
                        27.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                // =================================================
                // SUBTITLE
                // =================================================

                Text(
                    text =
                        subtitle,

                    color =
                        AdminGray,

                    fontSize =
                        13.sp,

                    lineHeight =
                        19.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                // =================================================
                // EMAIL LABEL
                // =================================================

                Text(
                    text =
                        "Email Address",

                    color =
                        AdminDarkCharcoal,

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.SemiBold
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                // =================================================
                // EMAIL FIELD
                // =================================================

                OutlinedTextField(
                    value =
                        email,

                    onValueChange = { value ->
                        email = value.trimStart()
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    singleLine =
                        true,

                    enabled =
                        !uiState.isLoading,

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.Email,

                            contentDescription =
                                "Email",

                            tint =
                                AdminForestGreen
                        )
                    },

                    placeholder = {

                        Text(
                            text =
                                "Enter your email address",

                            color =
                                AdminGray
                        )
                    },

                    shape =
                        RoundedCornerShape(14.dp),

                    colors =
                        OutlinedTextFieldDefaults.colors(

                            focusedBorderColor =
                                AdminForestGreen,

                            unfocusedBorderColor =
                                Color(0xFFD5DCD5),

                            focusedContainerColor =
                                AdminWhite,

                            unfocusedContainerColor =
                                AdminWhite,

                            focusedLeadingIconColor =
                                AdminForestGreen,

                            unfocusedLeadingIconColor =
                                AdminGray,

                            cursorColor =
                                AdminForestGreen,

                            disabledBorderColor =
                                Color(0xFFE0E4E0),

                            disabledTextColor =
                                AdminGray
                        )
                )

                Spacer(
                    modifier =
                        Modifier.height(18.dp)
                )

                // =================================================
                // CONTINUE BUTTON
                // =================================================

                Button(
                    onClick = {

                        if (isValidEmail(email)) {

                            viewModel.sendOtp(
                                email =
                                    email.trim(),

                                role =
                                    role
                            )
                        }
                    },

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(54.dp),

                    enabled =
                        isValidEmail(email) &&
                                !uiState.isLoading,

                    shape =
                        RoundedCornerShape(14.dp),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                AdminDarkGreen,

                            contentColor =
                                AdminWhite,

                            disabledContainerColor =
                                Color(0xFFD0D5D0),

                            disabledContentColor =
                                AdminWhite
                        )
                ) {

                    if (uiState.isLoading) {

                        CircularProgressIndicator(
                            modifier =
                                Modifier.size(21.dp),

                            color =
                                AdminWhite,

                            strokeWidth =
                                2.2.dp
                        )

                        Spacer(
                            modifier =
                                Modifier.width(9.dp)
                        )

                        Text(
                            text =
                                "Sending OTP...",

                            fontSize =
                                15.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                    } else {

                        Icon(
                            imageVector =
                                Icons.Default.Lock,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(18.dp)
                        )

                        Spacer(
                            modifier =
                                Modifier.width(8.dp)
                        )

                        Text(
                            text =
                                "Continue with Email OTP",

                            fontSize =
                                15.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                // =================================================
                // SECURITY TEXT
                // =================================================

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.Center,

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Lock,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(13.dp),

                        tint =
                            AdminGray
                    )

                    Spacer(
                        modifier =
                            Modifier.width(5.dp)
                    )

                    Text(
                        text =
                            "A 6-digit OTP will be sent to your email.",

                        color =
                            AdminGray,

                        fontSize =
                            11.sp,

                        textAlign =
                            TextAlign.Center
                    )
                }

                // =================================================
                // ERROR
                // =================================================

                uiState.errorMessage?.let { message ->

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    Surface(
                        modifier =
                            Modifier.fillMaxWidth(),

                        shape =
                            RoundedCornerShape(10.dp),

                        color =
                            Color(0xFFFFF1F2)
                    ) {

                        Text(
                            text =
                                message,

                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 12.dp,
                                        vertical = 9.dp
                                    ),

                            color =
                                Color(0xFFB91C1C),

                            fontSize =
                                12.sp,

                            fontWeight =
                                FontWeight.Medium,

                            textAlign =
                                TextAlign.Center,

                            lineHeight =
                                17.sp
                        )
                    }
                }
            }
        }
    }
}


// =============================================================
// EMAIL VALIDATION
// =============================================================

private fun isValidEmail(
    email: String
): Boolean {

    return android.util.Patterns.EMAIL_ADDRESS
        .matcher(email.trim())
        .matches()
}

