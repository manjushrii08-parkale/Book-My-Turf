package com.example.bookmyturf.screens.auth

import android.app.Activity

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.bookmyturf.R
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminLightGreen
import com.example.bookmyturf.viewmodel.AuthViewModel


@Composable
fun OtpVerificationScreen(
    email: String,
    role: String,
    onLoginSuccess: (
        role: String,
        token: String,
        userId: Int
    ) -> Unit,
    viewModel: AuthViewModel = viewModel()
) {

    // =========================================================
    // SYSTEM BARS
    // =========================================================

    val context = LocalContext.current

    DisposableEffect(Unit) {

        val activity = context as? Activity

        activity?.let {

            WindowCompat.setDecorFitsSystemWindows(
                it.window,
                true
            )

            val controller =
                WindowInsetsControllerCompat(
                    it.window,
                    it.window.decorView
                )

            // Status bar visible
            controller.show(
                WindowInsetsCompat.Type.statusBars()
            )

            // Navigation bar visible
            controller.show(
                WindowInsetsCompat.Type.navigationBars()
            )
        }

        onDispose {
            // Nothing required
        }
    }


    // =========================================================
    // OTP
    // =========================================================

    var otp by rememberSaveable {
        mutableStateOf("")
    }


    // =========================================================
    // VIEWMODEL STATE
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

            // userId is Int, so don't check userId != null
            if (
                !userRole.isNullOrBlank() &&
                !token.isNullOrBlank()
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
    // ROOT
    // =========================================================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                AdminDarkGreen
            )
    ) {


        // =====================================================
        // BACKGROUND IMAGE
        // =====================================================

        Image(
            painter = painterResource(
                id = R.drawable.login_background
            ),

            contentDescription =
                "Book My Turf",

            modifier =
                Modifier.fillMaxSize(),

            contentScale =
                ContentScale.FillBounds
        )


        // =====================================================
        // OTP CONTENT
        // =====================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .offset(
                    y = 65.dp
                )
                .padding(
                    horizontal = 28.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {


            // =================================================
            // TITLE
            // =================================================

            Text(
                text =
                    "BOOK • PLAY • WIN",

                color =
                    AdminLightGreen,

                fontSize =
                    19.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                letterSpacing =
                    1.5.sp,

                textAlign =
                    TextAlign.Center
            )


            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )


            // =================================================
            // SUBTITLE
            // =================================================

            Text(
                text =
                    "Verify your email\nand continue securely.",

                color =
                    Color.White.copy(
                        alpha = 0.90f
                    ),

                fontSize =
                    13.sp,

                lineHeight =
                    19.sp,

                fontWeight =
                    FontWeight.Medium,

                textAlign =
                    TextAlign.Center
            )


            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )


            // =================================================
            // FEATURES
            // =================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.Center,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text =
                        "⚡ Fast Verification",

                    color =
                        Color.White.copy(
                            alpha = 0.90f
                        ),

                    fontSize =
                        11.sp,

                    fontWeight =
                        FontWeight.Medium,

                    textAlign =
                        TextAlign.Center
                )


                Spacer(
                    modifier =
                        Modifier.width(10.dp)
                )


                Text(
                    text =
                        "•",

                    color =
                        AdminLightGreen,

                    fontSize =
                        14.sp,

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(
                    modifier =
                        Modifier.width(10.dp)
                )


                Text(
                    text =
                        "🔒 Secure Login",

                    color =
                        Color.White.copy(
                            alpha = 0.90f
                        ),

                    fontSize =
                        11.sp,

                    fontWeight =
                        FontWeight.Medium,

                    textAlign =
                        TextAlign.Center
                )
            }


            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )


            // =================================================
            // WHITE OTP CARD
            // =================================================

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(
                        24.dp
                    ),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color.White
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation =
                            8.dp
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


                    // =========================================
                    // EMAIL FIELD
                    // =========================================

                    OutlinedTextField(

                        value =
                            email,

                        onValueChange = {},

                        modifier =
                            Modifier.fillMaxWidth(),

                        singleLine =
                            true,

                        enabled =
                            false,

                        leadingIcon = {

                            Icon(

                                imageVector =
                                    Icons.Default.Email,

                                contentDescription =
                                    "Email",

                                tint =
                                    Color.White
                            )
                        },

                        shape =
                            RoundedCornerShape(
                                16.dp
                            ),

                        colors =
                            OutlinedTextFieldDefaults.colors(

                                focusedContainerColor =
                                    AdminDarkGreen,

                                unfocusedContainerColor =
                                    AdminDarkGreen,

                                disabledContainerColor =
                                    AdminDarkGreen,

                                focusedBorderColor =
                                    AdminLightGreen,

                                unfocusedBorderColor =
                                    AdminDarkGreen,

                                disabledBorderColor =
                                    AdminDarkGreen.copy(
                                        alpha = 0.50f
                                    ),

                                focusedTextColor =
                                    Color.White,

                                unfocusedTextColor =
                                    Color.White,

                                disabledTextColor =
                                    Color.White.copy(
                                        alpha = 0.90f
                                    ),

                                focusedLeadingIconColor =
                                    Color.White,

                                unfocusedLeadingIconColor =
                                    Color.White,

                                disabledLeadingIconColor =
                                    Color.White.copy(
                                        alpha = 0.75f
                                    )
                            )
                    )


                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )


                    // =========================================
                    // OTP FIELD
                    // =========================================

                    OutlinedTextField(

                        value =
                            otp,

                        onValueChange = { value ->

                            val digits =
                                value.filter {
                                    it.isDigit()
                                }

                            if (digits.length <= 6) {
                                otp = digits
                            }
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
                                    Icons.Default.Lock,

                                contentDescription =
                                    "OTP",

                                tint =
                                    Color.White
                            )
                        },

                        trailingIcon = {

                            if (otp.length == 6) {

                                Icon(

                                    imageVector =
                                        Icons.Default.CheckCircle,

                                    contentDescription =
                                        "OTP complete",

                                    tint =
                                        AdminLightGreen,

                                    modifier =
                                        Modifier.size(
                                            22.dp
                                        )
                                )
                            }
                        },

                        placeholder = {

                            Text(

                                text =
                                    "Enter 6-digit OTP",

                                color =
                                    Color.White.copy(
                                        alpha = 0.75f
                                    ),

                                fontSize =
                                    16.sp
                            )
                        },

                        shape =
                            RoundedCornerShape(
                                16.dp
                            ),

                        colors =
                            OutlinedTextFieldDefaults.colors(

                                focusedContainerColor =
                                    AdminDarkGreen,

                                unfocusedContainerColor =
                                    AdminDarkGreen,

                                disabledContainerColor =
                                    AdminDarkGreen,

                                focusedBorderColor =
                                    AdminLightGreen,

                                unfocusedBorderColor =
                                    AdminDarkGreen,

                                disabledBorderColor =
                                    AdminDarkGreen.copy(
                                        alpha = 0.50f
                                    ),

                                focusedTextColor =
                                    Color.White,

                                unfocusedTextColor =
                                    Color.White,

                                disabledTextColor =
                                    AdminGray,

                                focusedLeadingIconColor =
                                    Color.White,

                                unfocusedLeadingIconColor =
                                    Color.White,

                                disabledLeadingIconColor =
                                    Color.White.copy(
                                        alpha = 0.50f
                                    ),

                                focusedTrailingIconColor =
                                    AdminLightGreen,

                                unfocusedTrailingIconColor =
                                    AdminLightGreen,

                                cursorColor =
                                    AdminLightGreen
                            )
                    )


                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )


                    // =========================================
                    // VERIFY BUTTON
                    // =========================================

                    Button(

                        onClick = {

                            if (otp.length == 6) {

                                viewModel.verifyOtp(

                                    email =
                                        email.trim(),

                                    otp =
                                        otp,

                                    role =
                                        role
                                )
                            }
                        },

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp),

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
                                    AdminLightGreen,

                                contentColor =
                                    Color.White,

                                disabledContainerColor =
                                    AdminLightGreen.copy(
                                        alpha = 0.45f
                                    ),

                                disabledContentColor =
                                    Color.White.copy(
                                        alpha = 0.70f
                                    )
                            )
                    ) {


                        // =====================================
                        // LOADING
                        // =====================================

                        if (uiState.isLoading) {

                            CircularProgressIndicator(

                                modifier =
                                    Modifier.size(
                                        21.dp
                                    ),

                                color =
                                    Color.White,

                                strokeWidth =
                                    2.2.dp
                            )


                            Spacer(
                                modifier =
                                    Modifier.width(
                                        9.dp
                                    )
                            )


                            Text(

                                text =
                                    "Verifying...",

                                color =
                                    Color.White,

                                fontSize =
                                    15.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )

                        } else {


                            // =================================
                            // LOCK ICON
                            // =================================

                            Icon(

                                imageVector =
                                    Icons.Default.Lock,

                                contentDescription =
                                    null,

                                modifier =
                                    Modifier.size(
                                        18.dp
                                    ),

                                tint =
                                    Color.White
                            )


                            Spacer(
                                modifier =
                                    Modifier.width(
                                        8.dp
                                    )
                            )


                            // =================================
                            // VERIFY TEXT
                            // =================================

                            Text(

                                text =
                                    "VERIFY & CONTINUE",

                                color =
                                    Color.White,

                                fontSize =
                                    15.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }


                    Spacer(
                        modifier =
                            Modifier.height(13.dp)
                    )


                    // =========================================
                    // OTP MESSAGE
                    // =========================================

                    Text(

                        text =
                            "Enter the 6-digit OTP sent to your email.",

                        color =
                            AdminGray,

                        fontSize =
                            11.sp,

                        textAlign =
                            TextAlign.Center,

                        modifier =
                            Modifier.fillMaxWidth()
                    )


                    // =========================================
                    // ERROR MESSAGE
                    // =========================================

                    uiState.errorMessage?.let { message ->

                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
                        )

                        Text(

                            text =
                                message,

                            color =
                                Color(0xFFD32F2F),

                            fontSize =
                                12.sp,

                            fontWeight =
                                FontWeight.Medium,

                            textAlign =
                                TextAlign.Center,

                            modifier =
                                Modifier.fillMaxWidth()
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )


            // =================================================
            // SECURITY MESSAGE
            // =================================================

            Text(

                text =
                    "🔒 Secure OTP Verification",

                color =
                    Color.White.copy(
                        alpha = 0.80f
                    ),

                fontSize =
                    11.sp,

                fontWeight =
                    FontWeight.Medium,

                textAlign =
                    TextAlign.Center
            )
        }
    }
}