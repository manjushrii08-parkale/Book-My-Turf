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
fun OtpVerificationScreen(
    email: String,
    role: String,

    // role = String
    // token = String
    // userId = Int
    onLoginSuccess: (String, String, Int) -> Unit,

    viewModel: AuthViewModel = viewModel()
) {

    // =========================================================
    // OTP
    // =========================================================

    var otp by rememberSaveable {
        mutableStateOf("")
    }
    // =========================================================
    // UI STATE
    // =========================================================

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()


    // =========================================================
    // LOGIN SUCCESS
    // =========================================================

    LaunchedEffect(uiState.isLoggedIn) {

        if (uiState.isLoggedIn) {

            val userRole =
                viewModel.getUserRole()

            val token =
                viewModel.getToken()

            val userId =
                viewModel.getUserId()


            // =================================================
            // CHECK LOGIN DATA
            // =================================================

            if (
                !userRole.isNullOrBlank() &&
                !token.isNullOrBlank() &&
                userId != null
            ) {

                onLoginSuccess(
                    userRole,
                    token,
                    userId
                )
            }
        }
    }


    // =========================================================
    // ROLE TITLE
    // =========================================================

    val roleTitle = when (role) {

        "ADMIN" ->
            "TURF OWNER"

        "SUPER_ADMIN" ->
            "SUPER ADMIN"

        else ->
            "USER"
    }


    // =========================================================
    // MAIN TITLE
    // =========================================================

    val mainTitle = when (role) {

        "ADMIN" ->
            "Verify Turf Owner"

        "SUPER_ADMIN" ->
            "Verify Super Admin"

        else ->
            "Verify Your Email"
    }


    // =========================================================
    // SUBTITLE
    // =========================================================

    val subtitle = when (role) {

        "ADMIN" ->
            "Enter the verification code sent to your email"

        "SUPER_ADMIN" ->
            "Enter the verification code to continue"

        else ->
            "Enter the 6-digit code sent to your email"
    }


    // =========================================================
    // MAIN SCREEN
    // =========================================================

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // =====================================================
        // BACKGROUND
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
        // BRAND
        // =====================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 45.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(
                    id = R.drawable.logo
                ),

                contentDescription =
                    "Turf Logo",

                modifier =
                    Modifier.size(130.dp),

                contentScale =
                    ContentScale.Fit
            )


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            Text(
                text = "BOOK MY TURF",

                color = Color.White,

                fontSize = 23.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                letterSpacing = 2.sp
            )


            Spacer(
                modifier = Modifier.height(4.dp)
            )


            Text(
                text = "Play • Book • Enjoy",

                color =
                    Color.White.copy(
                        alpha = 0.85f
                    ),

                fontSize = 13.sp
            )
        }


        // =====================================================
        // OTP CARD
        // =====================================================

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(
                    Alignment.BottomCenter
                ),

            shape = RoundedCornerShape(
                topStart = 32.dp,
                topEnd = 32.dp
            ),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
                ),

            elevation =
                CardDefaults.cardElevation(
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
                            RoundedCornerShape(
                                50.dp
                            )
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

                        color =
                            Color(0xFF2E7D32),

                        fontSize = 11.sp,

                        fontWeight =
                            FontWeight.Bold,

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

                    color =
                        Color(0xFF14213D),

                    fontSize = 28.sp,

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(
                    modifier = Modifier.height(6.dp)
                )


                // =================================================
                // SUBTITLE
                // =================================================

                Text(
                    text = subtitle,

                    color =
                        Color(0xFF6B7280),

                    fontSize = 14.sp,

                    lineHeight = 20.sp
                )


                Spacer(
                    modifier = Modifier.height(22.dp)
                )


                // =================================================
                // EMAIL LABEL
                // =================================================

                Text(
                    text = "Verification email",

                    color =
                        Color(0xFF374151),

                    fontSize = 14.sp,

                    fontWeight =
                        FontWeight.SemiBold
                )


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                // =================================================
                // EMAIL
                // =================================================

                OutlinedTextField(
                    value = email,

                    onValueChange = {},

                    modifier =
                        Modifier.fillMaxWidth(),

                    enabled = false,

                    singleLine = true,

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.Email,

                            contentDescription =
                                "Email",

                            tint =
                                Color(0xFF2E7D32)
                        )
                    },

                    shape =
                        RoundedCornerShape(14.dp),

                    colors =
                        OutlinedTextFieldDefaults.colors(

                            disabledBorderColor =
                                Color(0xFFD1D5DB),

                            disabledTextColor =
                                Color(0xFF374151),

                            disabledLeadingIconColor =
                                Color(0xFF2E7D32)
                        )
                )


                Spacer(
                    modifier = Modifier.height(20.dp)
                )


                // =================================================
                // OTP LABEL
                // =================================================

                Text(
                    text =
                        "Verification Code",

                    color =
                        Color(0xFF374151),

                    fontSize = 14.sp,

                    fontWeight =
                        FontWeight.SemiBold
                )


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                // =================================================
                // OTP INPUT
                // =================================================

                OutlinedTextField(

                    value = otp,

                    onValueChange = { value ->

                        val digitsOnly =
                            value.filter {
                                it.isDigit()
                            }

                        if (
                            digitsOnly.length <= 6
                        ) {

                            otp =
                                digitsOnly
                        }
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    singleLine = true,

                    placeholder = {

                        Text(
                            text =
                                "Enter 6-digit OTP"
                        )
                    },

                    shape =
                        RoundedCornerShape(
                            14.dp
                        ),

                    colors =
                        OutlinedTextFieldDefaults.colors(

                            focusedBorderColor =
                                Color(0xFF2E7D32),

                            unfocusedBorderColor =
                                Color(0xFFD1D5DB),

                            cursorColor =
                                Color(0xFF2E7D32)
                        )
                )


                Spacer(
                    modifier = Modifier.height(20.dp)
                )


                // =================================================
                // VERIFY BUTTON
                // =================================================

                Button(

                    onClick = {

                        if (
                            otp.length == 6
                        ) {

                            viewModel.verifyOtp(

                                email = email,

                                otp = otp,

                                role = role
                            )
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),

                    enabled =
                        otp.length == 6 &&
                                !uiState.isLoading,

                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                Color(0xFF2E7D32),

                            disabledContainerColor =
                                Color(0xFFBDBDBD)
                        )
                ) {

                    if (
                        uiState.isLoading
                    ) {

                        CircularProgressIndicator(

                            modifier =
                                Modifier.size(23.dp),

                            color =
                                Color.White,

                            strokeWidth = 2.dp
                        )

                    } else {

                        Text(
                            text =
                                "Verify & Continue",

                            fontSize = 16.sp,

                            fontWeight =
                                FontWeight.Bold
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
                        "The verification code expires in 5 minutes.",

                    modifier =
                        Modifier.fillMaxWidth(),

                    textAlign =
                        TextAlign.Center,

                    color =
                        Color(0xFF9CA3AF),

                    fontSize = 12.sp
                )


                // =================================================
                // ERROR
                // =================================================

                uiState.errorMessage?.let { message ->

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    Text(
                        text = message,

                        modifier =
                            Modifier.fillMaxWidth(),

                        textAlign =
                            TextAlign.Center,

                        color =
                            Color(0xFFD32F2F),

                        fontSize = 13.sp,

                        fontWeight =
                            FontWeight.Medium
                    )
                }
            }
        }
    }
}