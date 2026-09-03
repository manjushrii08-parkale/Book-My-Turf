package com.example.bookmyturf.screens.user.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private data class UserBottomItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun UserBottomNavigation(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {

    val items = listOf(

        UserBottomItem(
            "Home",
            Icons.Default.Home
        ),

        UserBottomItem(
            "Favorites",
            Icons.Default.FavoriteBorder
        ),

        UserBottomItem(
            "Bookings",
            Icons.AutoMirrored.Filled.ReceiptLong
        ),

        UserBottomItem(
            "Profile",
            Icons.Default.Person
        )
    )

    NavigationBar(
        containerColor =
            MaterialTheme.colorScheme.surface,

        tonalElevation = 8.dp
    ) {

        items.forEachIndexed { index, item ->

            NavigationBarItem(

                selected =
                    selectedItem == index,

                onClick = {
                    onItemSelected(index)
                },

                icon = {

                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },

                label = {

                    Text(
                        text = item.title
                    )
                },

                colors =
                    NavigationBarItemDefaults.colors(

                        selectedIconColor =
                            MaterialTheme
                                .colorScheme
                                .primary,

                        selectedTextColor =
                            MaterialTheme
                                .colorScheme
                                .primary,

                        unselectedIconColor =
                            Color(0xFF7A8A83),

                        unselectedTextColor =
                            Color(0xFF7A8A83),

                        indicatorColor =
                            MaterialTheme
                                .colorScheme
                                .primaryContainer
                    )
            )
        }
    }
}