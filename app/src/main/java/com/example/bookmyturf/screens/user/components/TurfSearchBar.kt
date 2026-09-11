package com.example.bookmyturf.screens.user.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// ============================================================
// BOOKMYTURF PREMIUM COLORS
// ============================================================

private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val LightGreen = Color(0xFF7DBB4A)

private val White = Color(0xFFFFFFFF)
private val Charcoal = Color(0xFF1C1C1C)
private val Gray = Color(0xFF737373)

private val SoftBorder = Color(0xFFE3E8DF)
private val FocusBorder = Color(0xFF7DBB4A)
private val SoftGreen = Color(0xFFF0F5EA)


// ============================================================
// PREMIUM TURF SEARCH BAR
// ============================================================

@Composable
fun TurfSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {

    OutlinedTextField(

        // =====================================================
        // VALUE
        // =====================================================

        value = query,

        onValueChange = onQueryChange,

        // =====================================================
        // SIZE
        // =====================================================

        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp,
                vertical = 7.dp
            ),

        singleLine = true,

        // =====================================================
        // PLACEHOLDER
        // =====================================================

        placeholder = {

            Text(
                text = "Search turf, city or location",

                fontSize = 13.sp,

                fontWeight = FontWeight.Normal,

                color = Gray
            )
        },

        // =====================================================
        // SEARCH ICON
        // =====================================================

        leadingIcon = {

            Icon(
                imageVector = Icons.Default.Search,

                contentDescription = "Search",

                tint = ForestGreen
            )
        },

        // =====================================================
        // CLEAR BUTTON
        // =====================================================

        trailingIcon = {

            if (query.isNotBlank()) {

                IconButton(
                    onClick = {
                        onQueryChange("")
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.Clear,

                        contentDescription = "Clear search",

                        tint = Gray
                    )
                }
            }
        },

        // =====================================================
        // SHAPE
        // =====================================================

        shape = RoundedCornerShape(16.dp),

        // =====================================================
        // KEYBOARD
        // =====================================================

        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),

        // =====================================================
        // COLORS
        // =====================================================

        colors = OutlinedTextFieldDefaults.colors(

            // -------------------------------------------------
            // UNFOCUSED
            // -------------------------------------------------

            unfocusedContainerColor = White,

            unfocusedBorderColor = SoftBorder,

            unfocusedTextColor = Charcoal,

            // -------------------------------------------------
            // FOCUSED
            // -------------------------------------------------

            focusedContainerColor = SoftGreen.copy(
                alpha = 0.35f
            ),

            focusedBorderColor = FocusBorder,

            focusedTextColor = Charcoal,

            // -------------------------------------------------
            // CURSOR
            // -------------------------------------------------

            cursorColor = ForestGreen,

            // -------------------------------------------------
            // LEADING ICON
            // -------------------------------------------------

            focusedLeadingIconColor = ForestGreen,

            unfocusedLeadingIconColor = ForestGreen,

            // -------------------------------------------------
            // PLACEHOLDER
            // -------------------------------------------------

            focusedPlaceholderColor = Gray,

            unfocusedPlaceholderColor = Gray,

            // -------------------------------------------------
            // TRAILING ICON
            // -------------------------------------------------

            focusedTrailingIconColor = Gray,

            unfocusedTrailingIconColor = Gray
        ),

        // =====================================================
        // TEXT STYLE
        // =====================================================

        textStyle = androidx.compose.material3.MaterialTheme
            .typography
            .bodyMedium
            .copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Charcoal
            )
    )
}

