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
fun PhoneLoginScreen(
    role: String,
    onOtpSent: (String) -> Unit,
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
    // EMAIL
    // =========================================================

    var email by rememberSaveable {
        mutableStateOf("")
    }


    // =========================================================
    // VIEWMODEL STATE
    // =========================================================

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()


    // =========================================================
    // OTP NAVIGATION
    // =========================================================

    LaunchedEffect(uiState.otpSent) {

        if (uiState.otpSent) {

            onOtpSent(
                email.trim()
            )
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

            contentDescription = "Book My Turf",

            modifier = Modifier
                .fillMaxSize(),

            contentScale = ContentScale.FillBounds
        )


        // =====================================================
        // LOGIN CONTENT
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
                text = "BOOK • PLAY • WIN",

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
                    "Find your perfect turf\nand book your game easily.",

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
                        "⚡ Easy Booking",

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
                        "📍 Nearby Turfs",

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
            // WHITE LOGIN CARD
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

                        onValueChange = {
                            email = it.trimStart()
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

                                // WHITE EMAIL ICON
                                tint =
                                    Color.White
                            )
                        },

                        placeholder = {

                            Text(

                                text =
                                    "Email Address",

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

                                // ---------------------------------
                                // DARK GREEN BACKGROUND
                                // ---------------------------------

                                focusedContainerColor =
                                    AdminDarkGreen,

                                unfocusedContainerColor =
                                    AdminDarkGreen,

                                disabledContainerColor =
                                    AdminDarkGreen,

                                // ---------------------------------
                                // BORDER
                                // ---------------------------------

                                focusedBorderColor =
                                    AdminLightGreen,

                                unfocusedBorderColor =
                                    AdminDarkGreen,

                                disabledBorderColor =
                                    AdminDarkGreen.copy(
                                        alpha = 0.50f
                                    ),

                                // ---------------------------------
                                // TEXT
                                // ---------------------------------

                                focusedTextColor =
                                    Color.White,

                                unfocusedTextColor =
                                    Color.White,

                                disabledTextColor =
                                    AdminGray,

                                // ---------------------------------
                                // EMAIL ICON
                                // ---------------------------------

                                focusedLeadingIconColor =
                                    Color.White,

                                unfocusedLeadingIconColor =
                                    Color.White,

                                disabledLeadingIconColor =
                                    Color.White.copy(
                                        alpha = 0.50f
                                    ),

                                // ---------------------------------
                                // CURSOR
                                // ---------------------------------

                                cursorColor =
                                    AdminLightGreen
                            )
                    )


                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )


                    // =========================================
                    // CONTINUE BUTTON
                    // =========================================

                    Button(

                        onClick = {

                            val enteredEmail =
                                email.trim()

                            // Empty email
                            if (enteredEmail.isEmpty()) {
                                return@Button
                            }

                            // Invalid email
                            if (!isValidEmail(enteredEmail)) {
                                return@Button
                            }

                            // Send OTP
                            viewModel.sendOtp(

                                email =
                                    enteredEmail,

                                role =
                                    role
                            )
                        },

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp),

                        // Button remains clickable
                        // until OTP starts loading.
                        enabled =
                            !uiState.isLoading,

                        shape =
                            RoundedCornerShape(
                                16.dp
                            ),

                        colors =
                            ButtonDefaults.buttonColors(

                                // GREEN BUTTON
                                containerColor =
                                    AdminLightGreen,

                                // WHITE TEXT + ICON
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

                                // WHITE LOADER
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
                                    "Sending OTP...",

                                color =
                                    Color.White,

                                fontSize =
                                    15.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )

                        } else {


                            // =================================
                            // WHITE LOCK ICON
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
                            // WHITE CONTINUE TEXT
                            // =================================

                            Text(

                                text =
                                    "CONTINUE",

                                color =
                                    Color.White,

                                fontSize =
                                    16.sp,

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
                            "A 6-digit OTP will be sent to your email.",

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
                    "🔒 Secure OTP Login",

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


// =============================================================
// EMAIL VALIDATION
// =============================================================

private fun isValidEmail(
    email: String
): Boolean {

    return android.util.Patterns
        .EMAIL_ADDRESS
        .matcher(
            email.trim()
        )
        .matches()
}