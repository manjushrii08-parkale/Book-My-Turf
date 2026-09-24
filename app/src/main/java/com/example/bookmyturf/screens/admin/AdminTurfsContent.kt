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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bookmyturf.data.model.turf.Turf

// =============================================================
// PREMIUM ADMIN COLORS
// Same visual system as Subscription + Dashboard
// =============================================================

private val Background = Color(0xFF020907)
private val SurfaceDark = Color(0xFF06110D)
private val SurfaceElevated = Color(0xFF091711)
private val SurfaceHighlight = Color(0xFF0D2017)

private val PrimaryGreen = Color(0xFF7DBB4A)
private val LightGreen = Color(0xFFA8D86E)

private val PrimaryText = Color(0xFFF5F8F6)
private val SecondaryText = Color(0xFFA1AEA8)
private val MutedText = Color(0xFF687871)
private val Border = Color(0xFF183027)

private val ErrorRed = Color(0xFFFF6B6B)


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
            .background(Background)
            .padding(horizontal = 20.dp)
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
                    color = PrimaryText,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.3).sp
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
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
                    color = SecondaryText,
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = onAddTurf,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Color(0xFF061008)
                ),
                contentPadding = PaddingValues(
                    horizontal = 13.dp,
                    vertical = 9.dp
                )
            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Turf",
                    modifier = Modifier.size(18.dp)
                )

                Spacer(
                    modifier = Modifier.width(5.dp)
                )

                Text(
                    text = "Add Turf",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
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

            TurfLoadingState()

            return@Column
        }

        // =====================================================
        // ERROR
        // =====================================================

        if (!error.isNullOrBlank()) {

            TurfErrorState(
                message = error,
                onRetry = onRetry
            )

            return@Column
        }

        // =====================================================
        // EMPTY STATE
        // =====================================================

        if (turfs.isEmpty()) {

            TurfEmptyState(
                onAddTurf = onAddTurf
            )

            return@Column
        }

        // =====================================================
        // TURF LIST
        // =====================================================

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
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

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = SurfaceDark,
        border = BorderStroke(
            1.dp,
            Border
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
                            color = PrimaryText,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.2).sp
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
                                modifier = Modifier.size(15.dp),
                                tint = PrimaryGreen
                            )

                            Spacer(
                                modifier = Modifier.width(4.dp)
                            )

                            Text(
                                text = turf.city,
                                color = SecondaryText,
                                fontSize = 12.sp,
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
                    color = SurfaceElevated
                ) {

                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = PrimaryGreen.copy(alpha = 0.10f)
                        ) {

                            Box(
                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = PrimaryGreen
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
                                text = "LOCATION",
                                color = MutedText,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )

                            Spacer(
                                modifier = Modifier.height(3.dp)
                            )

                            Text(
                                text = turf.location,
                                color = PrimaryText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
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
                        text = "DESCRIPTION",
                        color = MutedText,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )

                    Text(
                        text = turf.description,
                        color = SecondaryText,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        maxLines = 2
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
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
                        containerColor = PrimaryGreen,
                        contentColor = Color(0xFF061008)
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
                        fontWeight = FontWeight.Bold
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
                            .height(46.dp),
                        shape = RoundedCornerShape(11.dp),
                        border = BorderStroke(
                            1.dp,
                            Border
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = SurfaceElevated,
                            contentColor = PrimaryText
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 10.dp
                        )
                    ) {

                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Turf",
                            modifier = Modifier.size(17.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Text(
                            text = "Edit Turf",
                            fontSize = 12.sp,
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
                            .height(46.dp),
                        shape = RoundedCornerShape(11.dp),
                        border = BorderStroke(
                            1.dp,
                            ErrorRed.copy(alpha = 0.35f)
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = SurfaceElevated,
                            contentColor = ErrorRed
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 10.dp
                        )
                    ) {

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Turf",
                            modifier = Modifier.size(17.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Text(
                            text = "Delete Turf",
                            fontSize = 12.sp,
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
                    color = ErrorRed.copy(alpha = 0.10f)
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = ErrorRed
                        )
                    }
                }
            },

            title = {
                Text(
                    text = "Delete Turf?",
                    color = PrimaryText,
                    fontWeight = FontWeight.Bold
                )
            },

            text = {
                Text(
                    text = "Are you sure you want to delete \"${turf.name}\"? This action cannot be undone.",
                    color = SecondaryText,
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
                        containerColor = ErrorRed,
                        contentColor = Color.White
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
                        color = SecondaryText,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },

            shape = RoundedCornerShape(20.dp),
            containerColor = SurfaceDark
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
                        Brush.verticalGradient(
                            colors = listOf(
                                SurfaceHighlight,
                                SurfaceElevated
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Surface(
                        modifier = Modifier.size(64.dp),
                        shape = RoundedCornerShape(18.dp),
                        color = PrimaryGreen.copy(alpha = 0.08f)
                    ) {

                        Box(
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = PrimaryGreen
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Text(
                        text = "No Images",
                        color = SecondaryText,
                        fontSize = 12.sp,
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
            // IMAGE COUNT
            // =================================================

            if (imageUrls.size > 1) {

                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    shape = RoundedCornerShape(50),
                    color = Color.Black.copy(alpha = 0.68f)
                ) {

                    Text(
                        text = "${pagerState.currentPage + 1}/${imageUrls.size}",
                        color = PrimaryText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        )
                    )
                }

                // =================================================
                // DOT INDICATOR
                // =================================================

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
                                        PrimaryGreen
                                    } else {
                                        PrimaryText.copy(alpha = 0.45f)
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
    value: String,
    icon: ImageVector
) {

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = SurfaceElevated
    ) {

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier.size(36.dp),
                shape = RoundedCornerShape(10.dp),
                color = PrimaryGreen.copy(alpha = 0.10f)
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = PrimaryGreen
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            Column {

                Text(
                    text = "PRICE / SLOT",
                    color = MutedText,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = value,
                    color = PrimaryText,
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

    val normalizedStatus = status
        .trim()
        .uppercase()

    Surface(
        shape = RoundedCornerShape(50),
        color = if (isActive) {
            PrimaryGreen.copy(alpha = 0.10f)
        } else {
            ErrorRed.copy(alpha = 0.08f)
        }
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 6.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(
                        if (isActive) {
                            PrimaryGreen
                        } else {
                            ErrorRed
                        }
                    )
            )

            Spacer(
                modifier = Modifier.width(6.dp)
            )

            Text(
                text = normalizedStatus,
                color = if (isActive) {
                    LightGreen
                } else {
                    ErrorRed
                },
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// =============================================================
// LOADING STATE
// =============================================================

@Composable
private fun TurfLoadingState() {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Surface(
            modifier = Modifier.size(62.dp),
            shape = RoundedCornerShape(18.dp),
            color = SurfaceElevated
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    color = PrimaryGreen,
                    strokeWidth = 2.5.dp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(13.dp)
        )

        Text(
            text = "Loading your turfs...",
            color = SecondaryText,
            fontSize = 12.sp
        )
    }
}


// =============================================================
// ERROR STATE
// =============================================================

@Composable
private fun TurfErrorState(
    message: String,
    onRetry: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Surface(
            modifier = Modifier.size(64.dp),
            shape = RoundedCornerShape(18.dp),
            color = ErrorRed.copy(alpha = 0.08f)
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(27.dp),
                    tint = ErrorRed
                )
            }
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Text(
            text = "Unable to load turfs",
            color = PrimaryText,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = message,
            color = SecondaryText,
            fontSize = 11.sp
        )

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        OutlinedButton(
            onClick = onRetry,
            shape = RoundedCornerShape(11.dp),
            border = BorderStroke(
                1.dp,
                Border
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = SurfaceElevated,
                contentColor = PrimaryText
            )
        ) {

            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )

            Spacer(
                modifier = Modifier.width(7.dp)
            )

            Text(
                text = "Retry",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


// =============================================================
// EMPTY STATE
// =============================================================

@Composable
private fun TurfEmptyState(
    onAddTurf: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Surface(
            modifier = Modifier.size(76.dp),
            shape = RoundedCornerShape(20.dp),
            color = SurfaceElevated
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.SportsSoccer,
                    contentDescription = null,
                    modifier = Modifier.size(34.dp),
                    tint = PrimaryGreen
                )
            }
        }

        Spacer(
            modifier = Modifier.height(17.dp)
        )

        Text(
            text = "No Turfs Added",
            color = PrimaryText,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = "Add your first turf to start managing bookings.",
            color = SecondaryText,
            fontSize = 12.sp
        )

        Spacer(
            modifier = Modifier.height(17.dp)
        )

        Button(
            onClick = onAddTurf,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryGreen,
                contentColor = Color(0xFF061008)
            )
        ) {

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )

            Spacer(
                modifier = Modifier.width(7.dp)
            )

            Text(
                text = "Add New Turf",
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