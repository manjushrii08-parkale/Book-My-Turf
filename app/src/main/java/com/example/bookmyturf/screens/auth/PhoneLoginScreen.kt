package com.example.bookmyturf.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    var email by rememberSaveable() {
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

            onOtpSent(email)
        }
    }


    // =========================================================
    // ROLE TITLE
    // =========================================================

    val roleTitle = when (role) {

        "ADMIN" -> "TURF OWNER"

        "SUPER_ADMIN" -> "SUPER ADMIN"

        else -> "USER"
    }


    // =========================================================
    // MAIN TITLE
    // =========================================================

    val mainTitle = when (role) {

        "ADMIN" -> "Welcome, Turf Owner"

        "SUPER_ADMIN" -> "Welcome, Super Admin"

        else -> "Welcome Back"
    }


    // =========================================================
    // SUBTITLE
    // =========================================================

    val subtitle = when (role) {

        "ADMIN" ->
            "Manage your turf, slots and bookings"

        "SUPER_ADMIN" ->
            "Manage users, turf owners and turfs"

        else ->
            "Book your favourite turf in seconds"
    }


    // =========================================================
    // MAIN SCREEN
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
        // DARK GRADIENT
        // =====================================================

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.20f),
                            Color.Black.copy(alpha = 0.40f),
                            Color.Black.copy(alpha = 0.75f)
                        )
                    )
                )
        )


        // =====================================================
        // TOP LOGO + BRAND
        // =====================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 45.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(
                    id = R.drawable.logo
                ),

                contentDescription = "Turf Logo",

                modifier = Modifier.size(130.dp),

                contentScale = ContentScale.Fit
            )


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            Text(
                text = "BOOK MY TURF",

                color = Color.White,

                fontSize = 23.sp,

                fontWeight = FontWeight.ExtraBold,

                letterSpacing = 2.sp
            )


            Spacer(
                modifier = Modifier.height(4.dp)
            )


            Text(
                text = "Play • Book • Enjoy",

                color = Color.White.copy(
                    alpha = 0.85f
                ),

                fontSize = 13.sp
            )
        }


        // =====================================================
        // LOGIN CARD
        // =====================================================

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),

            shape = RoundedCornerShape(
                topStart = 32.dp,
                topEnd = 32.dp
            ),

            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 12.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 28.dp
                    )
            ) {

                // =================================================
                // ROLE BADGE
                // =================================================

                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(50.dp)
                        )
                        .background(
                            Color(0xFFE8F5E9)
                        )
                        .padding(
                            horizontal = 14.dp,
                            vertical = 7.dp
                        )
                ) {

                    Text(
                        text = roleTitle,

                        color = Color(0xFF2E7D32),

                        fontSize = 11.sp,

                        fontWeight = FontWeight.Bold,

                        letterSpacing = 1.sp
                    )
                }


                Spacer(
                    modifier = Modifier.height(14.dp)
                )


                // =================================================
                // TITLE
                // =================================================

                Text(
                    text = mainTitle,

                    color = Color(0xFF14213D),

                    fontSize = 28.sp,

                    fontWeight = FontWeight.Bold
                )


                Spacer(
                    modifier = Modifier.height(6.dp)
                )


                // =================================================
                // SUBTITLE
                // =================================================

                Text(
                    text = subtitle,

                    color = Color(0xFF6B7280),

                    fontSize = 14.sp,

                    lineHeight = 20.sp
                )


                Spacer(
                    modifier = Modifier.height(24.dp)
                )


                // =================================================
                // EMAIL LABEL
                // =================================================

                Text(
                    text = "Email Address",

                    color = Color(0xFF374151),

                    fontSize = 14.sp,

                    fontWeight = FontWeight.SemiBold
                )


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                // =================================================
                // EMAIL INPUT
                // =================================================

                OutlinedTextField(

                    value = email,

                    onValueChange = { value ->

                        email = value.trimStart()
                    },

                    modifier = Modifier.fillMaxWidth(),

                    singleLine = true,

                    leadingIcon = {

                        Icon(
                            imageVector = Icons.Default.Email,

                            contentDescription = "Email",

                            tint = Color(0xFF2E7D32)
                        )
                    },

                    placeholder = {

                        Text(
                            text = "Enter your email address"
                        )
                    },

                    shape = RoundedCornerShape(14.dp),

                    colors = OutlinedTextFieldDefaults.colors(

                        focusedBorderColor =
                            Color(0xFF2E7D32),

                        unfocusedBorderColor =
                            Color(0xFFD1D5DB),

                        focusedLeadingIconColor =
                            Color(0xFF2E7D32),

                        unfocusedLeadingIconColor =
                            Color(0xFF6B7280),

                        cursorColor =
                            Color(0xFF2E7D32)
                    )
                )


                Spacer(
                    modifier = Modifier.height(20.dp)
                )


                // =================================================
                // SEND OTP BUTTON
                // =================================================

                Button(

                    onClick = {

                        if (isValidEmail(email)) {

                            viewModel.sendOtp(
                                email = email.trim(),
                                role = role
                            )
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),

                    enabled =
                        isValidEmail(email) &&
                                !uiState.isLoading,

                    shape = RoundedCornerShape(16.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            Color(0xFF2E7D32),

                        disabledContainerColor =
                            Color(0xFFBDBDBD)
                    )
                ) {

                    if (uiState.isLoading) {

                        CircularProgressIndicator(

                            modifier = Modifier.size(23.dp),

                            color = Color.White,

                            strokeWidth = 2.dp
                        )

                    } else {

                        Text(
                            text = "Continue with Email OTP",

                            fontSize = 16.sp,

                            fontWeight = FontWeight.Bold
                        )
                    }
                }


                Spacer(
                    modifier = Modifier.height(14.dp)
                )


                // =================================================
                // SECURITY MESSAGE
                // =================================================

                Text(
                    text =
                        "We'll send a 6-digit verification code to your email address.",

                    modifier = Modifier.fillMaxWidth(),

                    textAlign = TextAlign.Center,

                    color = Color(0xFF9CA3AF),

                    fontSize = 12.sp,

                    lineHeight = 17.sp
                )


                // =================================================
                // ERROR MESSAGE
                // =================================================

                uiState.errorMessage?.let { message ->

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Text(
                        text = message,

                        modifier = Modifier.fillMaxWidth(),

                        textAlign = TextAlign.Center,

                        color = Color(0xFFD32F2F),

                        fontSize = 13.sp,

                        fontWeight = FontWeight.Medium,

                        lineHeight = 18.sp
                    )
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