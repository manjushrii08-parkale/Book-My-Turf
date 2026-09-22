package com.example.bookmyturf.screens.user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================
// BOOKMYTURF DARK PREMIUM COLORS
// ============================================================

private val BackgroundDark = Color(0xFF020C09)
private val SearchBackground = Color(0xFF0B1C16)
private val BorderColor = Color(0xFF1D3029)

private val White = Color(0xFFFFFFFF)
private val SecondaryText = Color(0xFF9EAAA4)

private val LightGreen = Color(0xFF7DBB4A)
private val BrightGreen = Color(0xFF9FE15A)


// ============================================================
// PREMIUM TURF SEARCH BAR
// ============================================================

@Composable
fun TurfSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp,
                vertical = 10.dp
            )
            .background(
                color = SearchBackground,
                shape = RoundedCornerShape(16.dp)
            )
            .then(
                Modifier
            )
            .padding(
                horizontal = 6.dp,
                vertical = 4.dp
            ),

        verticalAlignment = Alignment.CenterVertically,

        horizontalArrangement = Arrangement.Start
    ) {

        // ====================================================
        // SEARCH ICON
        // ====================================================

        Icon(
            imageVector = Icons.Default.Search,

            contentDescription = "Search",

            modifier = Modifier
                .padding(start = 10.dp)
                .size(22.dp),

            tint = LightGreen
        )

        // ====================================================
        // SEARCH INPUT
        // ====================================================

        BasicTextField(

            value = query,

            onValueChange = onQueryChange,

            modifier = Modifier
                .weight(1f)
                .padding(
                    horizontal = 12.dp,
                    vertical = 11.dp
                ),

            singleLine = true,

            textStyle = TextStyle(
                color = White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            ),

            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),

            decorationBox = { innerTextField ->

                if (query.isEmpty()) {

                    androidx.compose.material3.Text(
                        text = "Search turf, city or location",

                        color = SecondaryText,

                        fontSize = 13.sp,

                        fontWeight = FontWeight.Normal
                    )
                }

                innerTextField()
            }
        )

        // ====================================================
        // CLEAR BUTTON
        // ====================================================

        if (query.isNotBlank()) {

            IconButton(
                onClick = {
                    onQueryChange("")
                }
            ) {

                Icon(
                    imageVector = Icons.Default.Clear,

                    contentDescription = "Clear search",

                    tint = SecondaryText
                )
            }
        }
    }
}