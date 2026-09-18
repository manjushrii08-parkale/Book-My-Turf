package com.example.bookmyturf.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmyturf.viewmodel.ReviewViewModel

// =============================================================
// COLORS
// =============================================================

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)

private val White = Color(0xFFFFFFFF)
private val OffWhite = Color(0xFFF7F9F5)

private val TextGray = Color(0xFF737373)
private val BorderGray = Color(0xFFE0E0E0)

private val ErrorRed = Color(0xFFB3261E)

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
    // HANDLE SUCCESS
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
            .background(OffWhite)
    ) {

        // =====================================================
        // CUSTOM TOP BAR
        // =====================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .background(White)
        ) {

            // Small visual breathing space
            Spacer(
                modifier = Modifier.height(6.dp)
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
                        tint = DarkGreen,
                        modifier = Modifier.size(23.dp)
                    )
                }

                // -------------------------------------------------
                // TITLE
                // -------------------------------------------------

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            start = 4.dp
                        )
                ) {

                    Text(
                        text = "Rate & Review",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkGreen
                    )

                    Spacer(
                        modifier = Modifier.height(2.dp)
                    )

                    Text(
                        text = "Share your experience",
                        fontSize = 12.sp,
                        color = TextGray
                    )
                }
            }

            HorizontalDivider(
                color = BorderGray,
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
                    vertical = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // =================================================
            // MAIN TITLE
            // =================================================

            Text(
                text = "How was your experience?",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DarkGreen,
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
                color = ForestGreen,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            // =================================================
            // DESCRIPTION
            // =================================================

            Text(
                text = "Your feedback helps other players choose the right turf.",
                fontSize = 13.sp,
                color = TextGray,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // =================================================
            // RATING CARD
            // =================================================

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFF0F7ED),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(
                        horizontal = 16.dp,
                        vertical = 24.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

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
                    color = DarkGreen
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                // =================================================
                // STARS
                // =================================================

                Row(
                    horizontalArrangement = Arrangement.Center
                ) {

                    for (rating in 1..5) {

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
                                    LightGreen
                                } else {
                                    BorderGray
                                },

                            modifier = Modifier
                                .size(48.dp)
                                .clickable {

                                    selectedRating = rating

                                    reviewViewModel.clearError()
                                }
                                .padding(4.dp)
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // =================================================
            // COMMENT FIELD
            // =================================================

            OutlinedTextField(

                value = comment,

                onValueChange = {

                    if (it.length <= 1000) {
                        comment = it
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),

                label = {
                    Text(
                        text = "Write a review"
                    )
                },

                placeholder = {
                    Text(
                        text = "Tell us about the turf, ground quality, cleanliness..."
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

                shape = RoundedCornerShape(14.dp)
            )

            // =================================================
            // ERROR MESSAGE
            // =================================================

            if (
                !uiState.errorMessage.isNullOrBlank()
            ) {

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = uiState.errorMessage.orEmpty(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = ErrorRed,
                    modifier = Modifier.fillMaxWidth()
                )
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

                    containerColor = DarkGreen,

                    disabledContainerColor =
                        Color(0xFFD6D6D6),

                    disabledContentColor =
                        Color(0xFF8A8A8A)
                )
            ) {

                if (uiState.isSubmitting) {

                    CircularProgressIndicator(

                        modifier = Modifier.size(22.dp),

                        strokeWidth = 2.dp,

                        color = White
                    )

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    Text(
                        text = "Submitting..."
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

                enabled = !uiState.isSubmitting
            ) {

                Text(
                    text = "Cancel",
                    color = ForestGreen,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
    }
}