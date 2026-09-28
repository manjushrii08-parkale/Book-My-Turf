package com.example.bookmyturf.screens.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.viewmodel.ReviewViewModel

// =============================================================
// PREMIUM BOOKMYTURF COLORS
// =============================================================

private val Background =
    Color(0xFF020907)

private val SurfaceDark =
    Color(0xFF06110D)

private val SurfaceElevated =
    Color(0xFF091711)

private val SurfaceHighlight =
    Color(0xFF0D2017)

private val PrimaryGreen =
    Color(0xFF7DBB4A)

private val LightGreen =
    Color(0xFFA8D86E)

private val BrightGreen =
    Color(0xFFC5F58B)

private val PrimaryText =
    Color(0xFFF5F8F6)

private val SecondaryText =
    Color(0xFFA1AEA8)

private val MutedText =
    Color(0xFF687871)

private val Border =
    Color(0xFF183027)

private val ErrorRed =
    Color(0xFFFF6B6B)


// =============================================================
// RATE & REVIEW SCREEN
// =============================================================

@Composable
fun RateReviewScreen(
    token: String,
    bookingId: Int,
    turfName: String,
    reviewViewModel: ReviewViewModel,
    onBackClick: () -> Unit,
    onReviewSubmitted: () -> Unit
) {

    // =========================================================
    // VIEWMODEL STATE
    // =========================================================

    val uiState by reviewViewModel.uiState.collectAsState()


    // =========================================================
    // RATING STATE
    // =========================================================

    var selectedRating by remember {
        mutableIntStateOf(0)
    }


    // =========================================================
    // COMMENT STATE
    // =========================================================

    var comment by remember {
        mutableStateOf("")
    }


    // =========================================================
    // HANDLE REVIEW SUCCESS
    // =========================================================

    LaunchedEffect(uiState.submitSuccess) {

        if (uiState.submitSuccess) {

            reviewViewModel.clearSubmitSuccess()

            onReviewSubmitted()
        }
    }


    // =========================================================
    // SCREEN
    // =========================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .navigationBarsPadding()
    ) {

        // =====================================================
        // TOP BAR
        // =====================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .background(SurfaceDark)
        ) {

            Spacer(
                modifier = Modifier.height(4.dp)
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 8.dp,
                        vertical = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // -------------------------------------------------
                // BACK BUTTON
                // -------------------------------------------------

                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(46.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = PrimaryText,
                        modifier = Modifier.size(23.dp)
                    )
                }


                // -------------------------------------------------
                // TITLE
                // -------------------------------------------------

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                ) {

                    Text(
                        text = "Rate & Review",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryText
                    )

                    Spacer(
                        modifier = Modifier.height(2.dp)
                    )

                    Text(
                        text = "Share your experience",
                        fontSize = 12.sp,
                        color = SecondaryText
                    )
                }
            }


            HorizontalDivider(
                color = Border,
                thickness = 1.dp
            )
        }


        // =====================================================
        // SCROLLABLE CONTENT
        // =====================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 26.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // =================================================
            // STAR ICON
            // =================================================

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        PrimaryGreen.copy(alpha = 0.12f)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = PrimaryGreen
                )
            }


            Spacer(
                modifier = Modifier.height(18.dp)
            )


            // =================================================
            // MAIN TITLE
            // =================================================

            Text(
                text = "How was your experience?",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryText,
                textAlign = TextAlign.Center
            )


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            // =================================================
            // TURF NAME
            // =================================================

            Text(
                text = turfName,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = LightGreen,
                textAlign = TextAlign.Center
            )


            Spacer(
                modifier = Modifier.height(7.dp)
            )


            // =================================================
            // DESCRIPTION
            // =================================================

            Text(
                text = "Your feedback helps other players choose the right turf.",
                fontSize = 13.sp,
                color = SecondaryText,
                lineHeight = 19.sp,
                textAlign = TextAlign.Center
            )


            Spacer(
                modifier = Modifier.height(28.dp)
            )


            // =================================================
            // RATING SECTION
            // =================================================

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = SurfaceElevated,
                border = BorderStroke(
                    width = 1.dp,
                    color = Border
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 24.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // -----------------------------------------
                    // RATING TITLE
                    // -----------------------------------------

                    Text(
                        text = when (selectedRating) {

                            0 -> "Tap a star to rate"
                            1 -> "Poor"
                            2 -> "Fair"
                            3 -> "Good"
                            4 -> "Very Good"
                            5 -> "Excellent"

                            else -> "Tap a star to rate"
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedRating > 0) {
                            LightGreen
                        } else {
                            SecondaryText
                        }
                    )


                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )


                    // -----------------------------------------
                    // STAR RATING
                    // -----------------------------------------

                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {

                        for (rating in 1..5) {

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable {

                                        selectedRating = rating

                                        reviewViewModel.clearError()
                                    },
                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    imageVector =
                                        if (rating <= selectedRating) {
                                            Icons.Filled.Star
                                        } else {
                                            Icons.Outlined.StarBorder
                                        },

                                    contentDescription =
                                        "$rating star rating",

                                    tint =
                                        if (rating <= selectedRating) {
                                            BrightGreen
                                        } else {
                                            MutedText
                                        },

                                    modifier = Modifier.size(34.dp)
                                )
                            }
                        }
                    }


                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )


                    // -----------------------------------------
                    // RATING COUNT
                    // -----------------------------------------

                    Text(
                        text =
                            if (selectedRating == 0) {
                                "Choose a rating from 1 to 5"
                            } else {
                                "$selectedRating out of 5 stars"
                            },
                        fontSize = 11.sp,
                        color = MutedText
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(24.dp)
            )


            // =================================================
            // REVIEW FIELD TITLE
            // =================================================

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Your review",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText
                )


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                // =================================================
                // REVIEW TEXT FIELD
                // =================================================

                OutlinedTextField(

                    value = comment,

                    onValueChange = {

                        if (it.length <= 1000) {

                            comment = it

                            reviewViewModel.clearError()
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(155.dp),

                    label = {
                        Text(
                            text = "Write a review"
                        )
                    },

                    placeholder = {
                        Text(
                            text =
                                "Tell us about the turf, ground quality, cleanliness..."
                        )
                    },

                    supportingText = {

                        Text(
                            text = "${comment.length}/1000",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    },

                    maxLines = 6,

                    shape = RoundedCornerShape(14.dp),

                    colors = OutlinedTextFieldDefaults.colors(

                        focusedTextColor =
                            PrimaryText,

                        unfocusedTextColor =
                            PrimaryText,

                        focusedContainerColor =
                            SurfaceDark,

                        unfocusedContainerColor =
                            SurfaceDark,

                        disabledContainerColor =
                            SurfaceDark,

                        focusedBorderColor =
                            PrimaryGreen,

                        unfocusedBorderColor =
                            Border,

                        focusedLabelColor =
                            PrimaryGreen,

                        unfocusedLabelColor =
                            SecondaryText,

                        cursorColor =
                            PrimaryGreen,

                        focusedPlaceholderColor =
                            MutedText,

                        unfocusedPlaceholderColor =
                            MutedText,

                        focusedSupportingTextColor =
                            SecondaryText,

                        unfocusedSupportingTextColor =
                            MutedText
                    )
                )
            }


            // =================================================
            // ERROR MESSAGE
            // =================================================

            if (
                !uiState.errorMessage.isNullOrBlank()
            ) {

                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = ErrorRed.copy(alpha = 0.10f),
                    border = BorderStroke(
                        width = 1.dp,
                        color = ErrorRed.copy(alpha = 0.25f)
                    )
                ) {

                    Text(
                        text = uiState.errorMessage.orEmpty(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = ErrorRed,
                        lineHeight = 19.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(26.dp)
            )


            // =================================================
            // SUBMIT BUTTON
            // =================================================

            Button(

                onClick = {

                    reviewViewModel.submitReview(

                        token = token,

                        bookingId = bookingId,

                        rating = selectedRating,

                        comment = comment
                    )
                },

                enabled =
                    selectedRating in 1..5 &&
                            !uiState.isSubmitting,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),

                shape = RoundedCornerShape(14.dp),

                colors = ButtonDefaults.buttonColors(

                    containerColor =
                        PrimaryGreen,

                    contentColor =
                        Color(0xFF10200F),

                    disabledContainerColor =
                        Color(0xFF1A2822),

                    disabledContentColor =
                        MutedText
                )
            ) {

                if (uiState.isSubmitting) {

                    CircularProgressIndicator(

                        modifier =
                            Modifier.size(21.dp),

                        strokeWidth = 2.dp,

                        color =
                            Color(0xFF10200F)
                    )


                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )


                    Text(
                        text = "Submitting...",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                } else {

                    Text(
                        text = "Submit Review",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            // =================================================
            // CANCEL
            // =================================================

            TextButton(

                onClick = onBackClick,

                enabled =
                    !uiState.isSubmitting
            ) {

                Text(
                    text = "Cancel",
                    color = SecondaryText,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }


            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
    }
}
