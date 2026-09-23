package com.example.bookmyturf.screens.user.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
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
// BOOKMYTURF PREMIUM DARK THEME
// ============================================================

private val SearchBackground = Color(0xFF071410)

private val PrimaryGreen = Color(0xFF7DBB4A)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)

private val Border = Color(0xFF1A3027)

// ============================================================
// TURF SEARCH BAR
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
                horizontal = 20.dp,
                vertical = 8.dp
            )
            .border(
                BorderStroke(
                    width = 1.dp,
                    color = Border
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(
                horizontal = 6.dp,
                vertical = 3.dp
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
                .padding(start = 8.dp)
                .size(21.dp),
            tint = PrimaryGreen
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
                    horizontal = 11.dp,
                    vertical = 11.dp
                ),

            singleLine = true,

            textStyle = TextStyle(
                color = PrimaryText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            ),

            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),

            decorationBox = { innerTextField ->

                if (query.isEmpty()) {
                    Text(
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
        // CLEAR SEARCH
        // ====================================================

        if (query.isNotBlank()) {
            IconButton(
                onClick = {
                    onQueryChange("")
                },
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear search",
                    modifier = Modifier.size(19.dp),
                    tint = SecondaryText
                )
            }
        }
    }
}
