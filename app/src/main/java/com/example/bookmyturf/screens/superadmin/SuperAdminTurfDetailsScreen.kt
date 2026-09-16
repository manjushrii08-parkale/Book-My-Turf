package com.example.bookmyturf.screens.superadmin
import androidx.compose.ui.draw.clip
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bookmyturf.data.model.SuperAdminTurf
import com.example.bookmyturf.viewmodel.SuperAdminTurfDetailsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

private val ScreenBackground = Color(0xFFF7F8F7)
private val DarkGreen = Color(0xFF173D20)
private val ForestGreen = Color(0xFF2E6B35)
private val TextDark = Color(0xFF263229)
private val TextGray = Color(0xFF788279)
private val BorderColor = Color(0xFFE1E6E1)

private val ActiveBackground = Color(0xFFE5F4E8)
private val ActiveText = Color(0xFF26733B)

private val BlockedBackground = Color(0xFFFFE9E7)
private val BlockedText = Color(0xFFB3261E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminTurfDetailsScreen(
    turfId: Int,
    token: String,
    onBackClick: () -> Unit,
    viewModel: SuperAdminTurfDetailsViewModel = viewModel()
) {
    val turf by viewModel.turf.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val actionMessage by viewModel.actionMessage.collectAsStateWithLifecycle()

    var showStatusDialog by remember {
        mutableStateOf(false)
    }

    var selectedImageUrl by remember {
        mutableStateOf<String?>(null)
    }

    BackHandler {
        onBackClick()
    }

    LaunchedEffect(turfId, token) {
        viewModel.loadTurfDetails(
            turfId = turfId,
            token = token
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        when {
            isLoading && turf == null -> {
                LoadingContent()
            }

            !errorMessage.isNullOrBlank() && turf == null -> {
                ErrorContent(
                    message = errorMessage.orEmpty(),
                    onRetry = {
                        viewModel.loadTurfDetails(
                            turfId = turfId,
                            token = token
                        )
                    }
                )
            }

            turf != null -> {
                val currentTurf = turf!!

                Scaffold(
                    containerColor = ScreenBackground,
                    topBar = {
                        TurfDetailsTopBar(
                            turfName = currentTurf.name
                                .orEmpty()
                                .ifBlank { "Turf Details" },
                            onBackClick = onBackClick
                        )
                    }
                ) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                            .padding(
                                horizontal = 16.dp,
                                vertical = 18.dp
                            ),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        TurfHeader(
                            turf = currentTurf
                        )

                        if (!actionMessage.isNullOrBlank()) {
                            SuccessMessage(
                                message = actionMessage.orEmpty()
                            )
                        }

                        MinimalSectionTitle(
                            title = "Images",
                            icon = Icons.Default.Image
                        )

                        TurfGallery(
                            imageUrls = currentTurf.image_urls.orEmpty(),
                            onImageClick = { imageUrl ->
                                selectedImageUrl = imageUrl
                            }
                        )

                        MinimalSectionTitle(
                            title = "Turf Information",
                            icon = Icons.Default.Description
                        )

                        InformationGroup {
                            DetailRow(
                                label = "Description",
                                value = currentTurf.description
                                    .orEmpty()
                                    .ifBlank { "Not available" }
                            )

                            DetailRow(
                                label = "Location",
                                value = currentTurf.location
                                    .orEmpty()
                                    .ifBlank { "Not available" }
                            )

                            DetailRow(
                                label = "Address",
                                value = currentTurf.address
                                    .orEmpty()
                                    .ifBlank { "Not available" }
                            )

                            DetailRow(
                                label = "City",
                                value = currentTurf.city
                                    .orEmpty()
                                    .ifBlank { "Not available" }
                            )
                        }

                        MinimalSectionTitle(
                            title = "Sports and Amenities",
                            icon = Icons.Default.SportsSoccer
                        )

                        InformationGroup {
                            DetailRow(
                                label = "Sports",
                                value = currentTurf.sports_types
                                    .orEmpty()
                                    .joinToString(", ")
                                    .ifBlank { "Not available" }
                            )

                            DetailRow(
                                label = "Amenities",
                                value = currentTurf.amenities
                                    .orEmpty()
                                    .joinToString(", ")
                                    .ifBlank { "Not available" }
                            )
                        }

                        MinimalSectionTitle(
                            title = "Pricing and Timings",
                            icon = Icons.Default.Payments
                        )

                        InformationGroup {
                            DetailRow(
                                label = "Hourly Price",
                                value = currentTurf.price
                                    ?.takeIf { it.isNotBlank() }
                                    ?.let { "₹$it" }
                                    ?: "Not available"
                            )

                            DetailRow(
                                label = "Opening Time",
                                value = currentTurf.opening_time
                                    .orEmpty()
                                    .ifBlank { "Not available" }
                            )

                            DetailRow(
                                label = "Closing Time",
                                value = currentTurf.closing_time
                                    .orEmpty()
                                    .ifBlank { "Not available" }
                            )
                        }

                        MinimalSectionTitle(
                            title = "Admin Information",
                            icon = Icons.Default.AdminPanelSettings
                        )

                        InformationGroup {
                            DetailRow(
                                label = "Admin ID",
                                value = currentTurf.admin_id?.toString()
                                    ?: "Not available"
                            )

                            DetailRow(
                                label = "Admin Name",
                                value = currentTurf.admin_name
                                    .orEmpty()
                                    .ifBlank { "Not available" }
                            )

                            DetailRow(
                                label = "Admin Email",
                                value = currentTurf.admin_email
                                    .orEmpty()
                                    .ifBlank { "Not available" }
                            )
                        }

                        MinimalSectionTitle(
                            title = "System Information",
                            icon = Icons.Default.CalendarToday
                        )

                        InformationGroup {
                            DetailRow(
                                label = "Turf ID",
                                value = "#${currentTurf.id}"
                            )

                            DetailRow(
                                label = "Created At",
                                value = formatDateTime(
                                    currentTurf.created_at
                                )
                            )

                            DetailRow(
                                label = "Updated At",
                                value = formatDateTime(
                                    currentTurf.updated_at
                                )
                            )
                        }

                        TurfStatusSection(
                            turf = currentTurf,
                            onStatusClick = {
                                showStatusDialog = true
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                    }
                }
            }

            else -> {
                EmptyContent()
            }
        }

        if (isLoading && turf != null) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp),
                shape = RoundedCornerShape(50.dp),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = ForestGreen
                    )

                    Text(
                        text = "Refreshing...",
                        color = DarkGreen,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }

    if (showStatusDialog && turf != null) {
        TurfStatusDialog(
            turf = turf!!,
            onDismiss = {
                showStatusDialog = false
            },
            onConfirm = {
                showStatusDialog = false

                val currentTurf = turf!!

                val isActive = currentTurf.status
                    .orEmpty()
                    .equals(
                        other = "ACTIVE",
                        ignoreCase = true
                    )

                if (isActive) {
                    viewModel.blockTurf(
                        turfId = turfId,
                        token = token
                    )
                } else {
                    viewModel.activateTurf(
                        turfId = turfId,
                        token = token
                    )
                }
            }
        )
    }

    selectedImageUrl?.let { imageUrl ->
        FullImageDialog(
            imageUrl = imageUrl,
            onDismiss = {
                selectedImageUrl = null
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TurfDetailsTopBar(
    turfName: String,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = turfName,
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "Turf Details",
                        color = TextGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DarkGreen
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = DarkGreen,
                navigationIconContentColor = DarkGreen
            )
        )

        HorizontalDivider(
            color = BorderColor,
            thickness = 1.dp
        )
    }
}

@Composable
private fun TurfHeader(
    turf: SuperAdminTurf
) {
    val status = turf.status
        .orEmpty()
        .ifBlank { "UNKNOWN" }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = turf.name
                    .orEmpty()
                    .ifBlank { "Unnamed Turf" },
                color = DarkGreen,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Turf ID: #${turf.id}",
                color = TextGray,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        StatusBadge(
            status = status
        )
    }
}

@Composable
private fun MinimalSectionTitle(
    title: String,
    icon: ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ForestGreen,
            modifier = Modifier.size(20.dp)
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = title,
            color = DarkGreen,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun InformationGroup(
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = BorderColor
        )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 4.dp
            ),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            content = content
        )
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 11.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            color = TextGray,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.labelMedium
        )

        Text(
            text = value,
            color = TextDark,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodyLarge
        )

        HorizontalDivider(
            color = BorderColor
        )
    }
}

@Composable
private fun TurfStatusSection(
    turf: SuperAdminTurf,
    onStatusClick: () -> Unit
) {
    val status = turf.status
        .orEmpty()
        .ifBlank { "UNKNOWN" }

    val isActive = status.equals(
        other = "ACTIVE",
        ignoreCase = true
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = BorderColor
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = ForestGreen,
                    modifier = Modifier.size(21.dp)
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "Turf Status",
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Current status",
                    color = TextGray
                )

                StatusBadge(
                    status = status
                )
            }

            Button(
                onClick = onStatusClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isActive) {
                        BlockedText
                    } else {
                        ForestGreen
                    }
                )
            ) {
                Icon(
                    imageVector = if (isActive) {
                        Icons.Default.Block
                    } else {
                        Icons.Default.CheckCircle
                    },
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = if (isActive) {
                        "Block Turf"
                    } else {
                        "Activate Turf"
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun StatusBadge(
    status: String
) {
    val isActive = status.equals(
        other = "ACTIVE",
        ignoreCase = true
    )

    Surface(
        color = if (isActive) {
            ActiveBackground
        } else {
            BlockedBackground
        },
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(
            text = status.uppercase(Locale.getDefault()),
            modifier = Modifier.padding(
                horizontal = 11.dp,
                vertical = 6.dp
            ),
            color = if (isActive) {
                ActiveText
            } else {
                BlockedText
            },
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun TurfGallery(
    imageUrls: List<String>,
    onImageClick: (String) -> Unit
) {
    if (imageUrls.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(125.dp)
                .background(
                    color = Color(0xFFEFF3EF),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No turf images available",
                color = TextGray
            )
        }
    } else {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = imageUrls,
                key = { imageUrl -> imageUrl }
            ) { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Turf image",
                    modifier = Modifier
                        .size(
                            width = 190.dp,
                            height = 125.dp
                        )
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            onImageClick(imageUrl)
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun SuccessMessage(
    message: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = ActiveBackground,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = ActiveText
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = message,
                color = ActiveText,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun TurfStatusDialog(
    turf: SuperAdminTurf,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val isActive = turf.status
        .orEmpty()
        .equals(
            other = "ACTIVE",
            ignoreCase = true
        )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isActive) {
                    "Block Turf?"
                } else {
                    "Activate Turf?"
                },
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = if (isActive) {
                    "Blocking this turf may prevent users from booking it."
                } else {
                    "This turf will become available for booking after activation."
                }
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = if (isActive) {
                        "Block"
                    } else {
                        "Activate"
                    },
                    color = if (isActive) {
                        BlockedText
                    } else {
                        ForestGreen
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancel"
                )
            }
        },
        shape = RoundedCornerShape(18.dp)
    )
}

@Composable
private fun FullImageDialog(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Turf Image",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = onDismiss
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }
        },
        text = {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Selected turf image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Fit
            )
        },
        confirmButton = {},
        shape = RoundedCornerShape(18.dp)
    )
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = ForestGreen
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Loading turf details...",
            color = TextGray
        )
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message.ifBlank {
                "Something went wrong."
            },
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = ForestGreen
            )
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = "Retry"
            )
        }
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Turf details not found.",
            color = TextGray
        )
    }
}

private fun formatDateTime(
    dateTime: String?
): String {
    if (dateTime.isNullOrBlank()) {
        return "Not available"
    }

    val inputPatterns = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
        "yyyy-MM-dd'T'HH:mm:ssXXX",
        "yyyy-MM-dd HH:mm:ss"
    )

    for (pattern in inputPatterns) {
        try {
            val parser = SimpleDateFormat(
                pattern,
                Locale.getDefault()
            )

            val parsedDate = parser.parse(dateTime)

            if (parsedDate != null) {
                return SimpleDateFormat(
                    "dd MMM yyyy, hh:mm a",
                    Locale.getDefault()
                ).format(parsedDate)
            }
        } catch (_: Exception) {
            // Try the next date format.
        }
    }

    return dateTime
}