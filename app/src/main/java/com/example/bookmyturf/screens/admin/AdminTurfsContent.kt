package com.example.bookmyturf.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bookmyturf.data.model.turf.Turf
import com.example.bookmyturf.ui.theme.AdminDarkCharcoal
import com.example.bookmyturf.ui.theme.AdminDarkGreen
import com.example.bookmyturf.ui.theme.AdminForestGreen
import com.example.bookmyturf.ui.theme.AdminGray
import com.example.bookmyturf.ui.theme.AdminLightGreen
import com.example.bookmyturf.ui.theme.AdminOffWhite
import com.example.bookmyturf.ui.theme.AdminWhite

// =============================================================
// LOCAL COLORS
// =============================================================

private val AdminRed = Color(0xFFB91C1C)
private val AdminRedLight = Color(0xFFFFF1F2)
private val AdminRedBorder = Color(0xFFE5B8B8)


// =============================================================
// ADMIN TURFS CONTENT
// =============================================================

@Composable
fun AdminTurfsContent(
    modifier: Modifier = Modifier,
    turfs: List<Turf>,
    isLoading: Boolean,
    error: String?,
    onAddTurf: () -> Unit,
    onEditTurf: (Turf) -> Unit,
    onManageSlots: (Turf) -> Unit,
    onDeleteTurf: (Turf) -> Unit,
    onRetry: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AdminOffWhite)
            .padding(horizontal = 16.dp)
    ) {

        // =====================================================
        // HEADER
        // =====================================================

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "My Turfs",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = AdminDarkCharcoal
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = if (turfs.isEmpty()) {
                        "Manage your turf grounds"
                    } else {
                        "${turfs.size} turf${
                            if (turfs.size == 1) {
                                ""
                            } else {
                                "s"
                            }
                        } available"
                    },
                    color = AdminGray,
                    fontSize = 13.sp
                )
            }

            Button(
                onClick = onAddTurf,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AdminDarkGreen,
                    contentColor = AdminWhite
                )
            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Turf"
                )

                Spacer(
                    modifier = Modifier.width(5.dp)
                )

                Text(
                    text = "Add Turf",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        // =====================================================
        // LOADING
        // =====================================================

        if (isLoading) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                CircularProgressIndicator(
                    color = AdminForestGreen
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Loading your turfs...",
                    color = AdminGray,
                    fontSize = 14.sp
                )
            }

            return@Column
        }

        // =====================================================
        // ERROR
        // =====================================================

        if (!error.isNullOrBlank()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Surface(
                    modifier = Modifier.size(76.dp),
                    shape = CircleShape,
                    color = Color(0xFFFEE2E2)
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            modifier = Modifier.size(34.dp),
                            tint = AdminRed
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = "Unable to load turfs",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AdminDarkCharcoal
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = error,
                    color = AdminGray,
                    fontSize = 13.sp
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    onClick = onRetry,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AdminDarkGreen,
                        contentColor = AdminWhite
                    )
                ) {

                    Text(
                        text = "Retry",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            return@Column
        }

        // =====================================================
        // EMPTY STATE
        // =====================================================

        if (turfs.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Surface(
                    modifier = Modifier.size(82.dp),
                    shape = CircleShape,
                    color = AdminLightGreen.copy(alpha = 0.20f)
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.SportsSoccer,
                            contentDescription = null,
                            modifier = Modifier.size(42.dp),
                            tint = AdminForestGreen
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Text(
                    text = "No Turfs Added",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AdminDarkCharcoal
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Add your first turf to start managing bookings.",
                    color = AdminGray,
                    fontSize = 14.sp
                )

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Button(
                    onClick = onAddTurf,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AdminDarkGreen,
                        contentColor = AdminWhite
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text(
                        text = "Add New Turf",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            return@Column
        }

        // =====================================================
        // TURF LIST
        // =====================================================

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(
                bottom = 30.dp
            )
        ) {

            items(
                items = turfs,
                key = { it.id }
            ) { turf ->

                TurfAdminCard(
                    turf = turf,

                    onEdit = {
                        onEditTurf(turf)
                    },

                    onManageSlots = {
                        onManageSlots(turf)
                    },

                    onDelete = {
                        onDeleteTurf(turf)
                    }
                )
            }
        }
    }
}


// =============================================================
// TURF ADMIN CARD
// =============================================================

@Composable
private fun TurfAdminCard(
    turf: Turf,
    onEdit: () -> Unit,
    onManageSlots: () -> Unit,
    onDelete: () -> Unit
) {

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = AdminWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            // =================================================
            // IMAGE GALLERY
            // =================================================

            TurfImageGallery(
                imageUrls = turf.imageUrls
            )

            // =================================================
            // INFORMATION
            // =================================================

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                // =================================================
                // NAME + STATUS
                // =================================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = turf.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = AdminDarkCharcoal
                        )

                        Spacer(
                            modifier = Modifier.height(5.dp)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = AdminForestGreen
                            )

                            Spacer(
                                modifier = Modifier.width(4.dp)
                            )

                            Text(
                                text = turf.city,
                                color = AdminGray,
                                fontSize = 13.sp,
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    TurfStatusBadge(
                        status = turf.status
                    )
                }

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                // =================================================
                // LOCATION
                // =================================================

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = AdminOffWhite
                ) {

                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = AdminLightGreen.copy(alpha = 0.20f)
                        ) {

                            Box(
                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(19.dp),
                                    tint = AdminForestGreen
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier.width(10.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Location",
                                color = AdminGray,
                                fontSize = 11.sp
                            )

                            Spacer(
                                modifier = Modifier.height(2.dp)
                            )

                            Text(
                                text = turf.location,
                                color = AdminDarkCharcoal,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 2
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                // =================================================
                // PRICE
                // =================================================

                TurfInfoBox(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Price / Slot",
                    value = "₹${formatPrice(turf.price)}",
                    icon = Icons.Default.Star
                )

                // =================================================
                // DESCRIPTION
                // =================================================

                if (!turf.description.isNullOrBlank()) {

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    Text(
                        text = turf.description,
                        color = AdminGray,
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        maxLines = 2
                    )
                }

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                // =================================================
                // MANAGE SLOTS
                // =================================================

                Button(
                    onClick = onManageSlots,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(11.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AdminDarkGreen,
                        contentColor = AdminWhite
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Manage Slots",
                        modifier = Modifier.size(19.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(7.dp)
                    )

                    Text(
                        text = "Manage Slots",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                // =================================================
                // EDIT + DELETE
                // =================================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    // =================================================
                    // EDIT
                    // =================================================

                    OutlinedButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(11.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = AdminForestGreen
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = AdminWhite,
                            contentColor = AdminForestGreen
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 12.dp
                        )
                    ) {

                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Turf",
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Text(
                            text = "Edit Turf",
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // =================================================
                    // DELETE
                    // =================================================

                    OutlinedButton(
                        onClick = {
                            showDeleteDialog = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(11.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = AdminRed
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = AdminWhite,
                            contentColor = AdminRed
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 12.dp
                        )
                    ) {

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Turf",
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Text(
                            text = "Delete Turf",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }

    // =========================================================
    // DELETE CONFIRMATION DIALOG
    // =========================================================

    if (showDeleteDialog) {

        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },

            icon = {
                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = CircleShape,
                    color = AdminRedLight
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                            tint = AdminRed
                        )
                    }
                }
            },

            title = {
                Text(
                    text = "Delete Turf?",
                    fontWeight = FontWeight.Bold,
                    color = AdminDarkCharcoal
                )
            },

            text = {
                Text(
                    text = "Are you sure you want to delete \"${turf.name}\"? This action cannot be undone.",
                    color = AdminGray,
                    lineHeight = 20.sp
                )
            },

            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AdminRed,
                        contentColor = AdminWhite
                    )
                ) {

                    Text(
                        text = "Delete",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {

                    Text(
                        text = "Cancel",
                        color = AdminGray,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },

            shape = RoundedCornerShape(20.dp),
            containerColor = AdminWhite
        )
    }
}


// =============================================================
// IMAGE GALLERY
// =============================================================

@Composable
private fun TurfImageGallery(
    imageUrls: List<String>
) {

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = {
            if (imageUrls.isEmpty()) {
                1
            } else {
                imageUrls.size
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.15f)
            .clip(
                RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp
                )
            )
    ) {

        if (imageUrls.isEmpty()) {

            // =================================================
            // NO IMAGE
            // =================================================

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        AdminLightGreen.copy(alpha = 0.18f)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = AdminForestGreen
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "No Images",
                        color = AdminForestGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

        } else {

            // =================================================
            // SWIPEABLE IMAGES
            // =================================================

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->

                val rawUrl = imageUrls[page]

                val imageUrl = buildImageUrl(rawUrl)

                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Turf image ${page + 1}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // =================================================
            // IMAGE COUNT + DOTS
            // =================================================

            if (imageUrls.size > 1) {

                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    shape = RoundedCornerShape(50),
                    color = Color.Black.copy(alpha = 0.65f)
                ) {

                    Text(
                        text = "${pagerState.currentPage + 1}/${imageUrls.size}",
                        color = AdminWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    imageUrls.forEachIndexed { index, _ ->

                        Box(
                            modifier = Modifier
                                .size(
                                    if (pagerState.currentPage == index) {
                                        8.dp
                                    } else {
                                        6.dp
                                    }
                                )
                                .clip(CircleShape)
                                .background(
                                    if (pagerState.currentPage == index) {
                                        AdminWhite
                                    } else {
                                        AdminWhite.copy(alpha = 0.5f)
                                    }
                                )
                        )
                    }
                }
            }
        }
    }
}


// =============================================================
// TURF INFO BOX
// =============================================================

@Composable
private fun TurfInfoBox(
    modifier: Modifier,
    title: String,
    value: String,
    icon: ImageVector
) {

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = AdminOffWhite
    ) {

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier.size(36.dp),
                shape = RoundedCornerShape(10.dp),
                color = AdminLightGreen.copy(alpha = 0.20f)
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = AdminForestGreen
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            Column {

                Text(
                    text = title,
                    color = AdminGray,
                    fontSize = 11.sp
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = value,
                    color = AdminDarkCharcoal,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
    }
}


// =============================================================
// STATUS BADGE
// =============================================================

@Composable
private fun TurfStatusBadge(
    status: String
) {

    val isActive = status.equals(
        "ACTIVE",
        ignoreCase = true
    )

    Surface(
        shape = RoundedCornerShape(50),
        color = if (isActive) {
            AdminLightGreen.copy(alpha = 0.20f)
        } else {
            Color(0xFFFEE2E2)
        }
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 11.dp,
                vertical = 6.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(
                        if (isActive) {
                            AdminForestGreen
                        } else {
                            AdminRed
                        }
                    )
            )

            Spacer(
                modifier = Modifier.width(6.dp)
            )

            Text(
                text = status.uppercase(),
                color = if (isActive) {
                    AdminForestGreen
                } else {
                    AdminRed
                },
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// =============================================================
// IMAGE URL BUILDER
// =============================================================

private fun buildImageUrl(
    imageUrl: String
): String {

    val cleanUrl = imageUrl.trim()

    if (
        cleanUrl.startsWith("http://") ||
        cleanUrl.startsWith("https://")
    ) {
        return cleanUrl
    }

    // =========================================================
    // ANDROID EMULATOR -> LARAVEL
    // =========================================================

    return "http://10.0.2.2:8000/${cleanUrl.trimStart('/')}"
}


// =============================================================
// PRICE FORMAT
// =============================================================

private fun formatPrice(
    price: Double
): String {

    return if (price % 1.0 == 0.0) {

        price
            .toInt()
            .toString()

    } else {

        "%.2f".format(price)
    }
}

