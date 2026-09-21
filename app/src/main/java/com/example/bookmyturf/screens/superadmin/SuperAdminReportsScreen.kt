package com.example.bookmyturf.screens.superadmin

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast

import androidx.activity.compose.BackHandler

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.SportsSoccer

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.bookmyturf.data.model.SuperAdminBooking
import com.example.bookmyturf.data.model.SuperAdminDashboardData

import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


private val DarkGreen = ComposeColor(0xFF173D20)
private val ForestGreen = ComposeColor(0xFF2E6B35)
private val LightGreen = ComposeColor(0xFFEAF4E7)
private val PaleGreen = ComposeColor(0xFFF5FAF3)
private val TextDark = ComposeColor(0xFF1C281E)
private val TextGray = ComposeColor(0xFF6B756D)
private val BorderGreen = ComposeColor(0xFFD9E7D7)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminReportsScreen(
    data: SuperAdminDashboardData,
    bookings: List<SuperAdminBooking>,
    onBackClick: () -> Unit,
    onDashboardClick: () -> Unit,
    onUsersClick: () -> Unit,
    onAdminsClick: () -> Unit
) {

    val context = androidx.compose.ui.platform.LocalContext.current

    BackHandler {
        onBackClick()
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text = "Reports",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen
                        )

                        Text(
                            text = "BookMyTurf analytics",
                            fontSize = 12.sp,
                            color = TextGray
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
                    containerColor = ComposeColor.White,
                    titleContentColor = DarkGreen,
                    navigationIconContentColor = DarkGreen
                )
            )
        },

        bottomBar = {

            NavigationBar(
                containerColor = ComposeColor.White,
                tonalElevation = 5.dp
            ) {

                // Dashboard
                NavigationBarItem(

                    selected = false,

                    onClick = {
                        onDashboardClick()
                    },

                    icon = {

                        Icon(
                            imageVector = Icons.Default.Dashboard,
                            contentDescription = "Dashboard"
                        )
                    },

                    label = {
                        Text(
                            text = "Dashboard"
                        )
                    }
                )


                // Users
                NavigationBarItem(

                    selected = false,

                    onClick = {
                        onUsersClick()
                    },

                    icon = {

                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = "Users"
                        )
                    },

                    label = {
                        Text(
                            text = "Users"
                        )
                    }
                )


                // Admins
                NavigationBarItem(

                    selected = false,

                    onClick = {
                        onAdminsClick()
                    },

                    icon = {

                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admins"
                        )
                    },

                    label = {
                        Text(
                            text = "Admins"
                        )
                    }
                )


                // Reports
                NavigationBarItem(

                    selected = true,

                    onClick = {
                        // Already on Reports screen
                    },

                    icon = {

                        Icon(
                            imageVector = Icons.Default.Assessment,
                            contentDescription = "Reports"
                        )
                    },

                    label = {
                        Text(
                            text = "Reports"
                        )
                    }
                )
            }
        },

        containerColor = ComposeColor(0xFFF7FAF7)

    ) { innerPadding ->


        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),

            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 18.dp,
                bottom = 24.dp
            ),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {


            item {

                SectionTitle(
                    title = "Platform overview",
                    subtitle = "Current BookMyTurf platform statistics"
                )
            }


            item {

                SummaryGrid(
                    data = data
                )
            }


            item {

                SubscriptionOverviewCard(
                    data = data
                )
            }


            item {

                SectionTitle(
                    title = "Booking reports",
                    subtitle = "${bookings.size} booking records available"
                )
            }


            if (bookings.isEmpty()) {

                item {

                    EmptyBookingsCard()
                }

            } else {

                items(
                    items = bookings,
                    key = { booking ->
                        booking.id
                    }
                ) { booking ->

                    BookingReportCard(
                        booking = booking
                    )
                }
            }


            item {

                DownloadReportCard(

                    onDownloadClick = {

                        generateDetailedReportPdf(
                            context = context,
                            data = data,
                            bookings = bookings
                        )
                    }
                )
            }
        }
    }
}


@Composable
private fun SectionTitle(
    title: String,
    subtitle: String
) {

    Column {

        Text(
            text = title,
            color = TextDark,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = subtitle,
            color = TextGray,
            fontSize = 12.sp
        )
    }
}


@Composable
private fun SummaryGrid(
    data: SuperAdminDashboardData
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            SummaryMetricCard(

                modifier = Modifier.weight(1f),

                title = "Users",

                value = data.users.total.toString(),

                subtitle = "${data.users.active} active",

                icon = Icons.Default.Groups
            )


            SummaryMetricCard(

                modifier = Modifier.weight(1f),

                title = "Admins",

                value = data.admins.total.toString(),

                subtitle = "${data.admins.active} active",

                icon = Icons.Default.Groups
            )
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            SummaryMetricCard(

                modifier = Modifier.weight(1f),

                title = "Turfs",

                value = data.turfs.total.toString(),

                subtitle = "Registered turfs",

                icon = Icons.Default.SportsSoccer
            )


            SummaryMetricCard(

                modifier = Modifier.weight(1f),

                title = "Bookings",

                value = data.bookings.total.toString(),

                subtitle = "Total bookings",

                icon = Icons.Default.ReceiptLong
            )
        }
    }
}


@Composable
private fun SummaryMetricCard(
    modifier: Modifier,
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {

    Card(

        modifier = modifier,

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(
            containerColor = ComposeColor.White
        ),

        border = BorderStroke(
            width = 1.dp,
            color = BorderGreen
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(15.dp)
        ) {

            Box(

                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(LightGreen),

                contentAlignment = Alignment.Center
            ) {

                Icon(

                    imageVector = icon,

                    contentDescription = null,

                    tint = ForestGreen,

                    modifier = Modifier.size(20.dp)
                )
            }


            Spacer(
                modifier = Modifier.height(13.dp)
            )


            Text(

                text = value,

                color = DarkGreen,

                fontSize = 25.sp,

                fontWeight = FontWeight.Bold
            )


            Spacer(
                modifier = Modifier.height(2.dp)
            )


            Text(

                text = title,

                color = TextDark,

                fontSize = 13.sp,

                fontWeight = FontWeight.SemiBold
            )


            Spacer(
                modifier = Modifier.height(3.dp)
            )


            Text(

                text = subtitle,

                color = TextGray,

                fontSize = 11.sp,

                maxLines = 1,

                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
private fun SubscriptionOverviewCard(
    data: SuperAdminDashboardData
) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = ComposeColor.White
        ),

        border = BorderStroke(
            width = 1.dp,
            color = BorderGreen
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(

                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(LightGreen),

                    contentAlignment = Alignment.Center
                ) {

                    Icon(

                        imageVector = Icons.Default.Payments,

                        contentDescription = null,

                        tint = ForestGreen
                    )
                }


                Spacer(
                    modifier = Modifier.width(12.dp)
                )


                Column {

                    Text(

                        text = "Subscription overview",

                        color = TextDark,

                        fontSize = 16.sp,

                        fontWeight = FontWeight.Bold
                    )


                    Text(

                        text = "Subscription and revenue summary",

                        color = TextGray,

                        fontSize = 12.sp
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            HorizontalDivider(
                color = BorderGreen
            )


            Spacer(
                modifier = Modifier.height(12.dp)
            )


            ReportValueRow(
                label = "Total subscriptions",
                value = data.subscriptions.total.toString()
            )


            ReportValueRow(
                label = "Active subscriptions",
                value = data.subscriptions.active.toString()
            )


            ReportValueRow(
                label = "Inactive subscriptions",
                value = data.subscriptions.inactive.toString()
            )


            ReportValueRow(
                label = "Paid subscriptions",
                value = data.subscriptions.paid.toString()
            )


            ReportValueRow(
                label = "Free trials",
                value = data.subscriptions.free_trials.toString()
            )


            ReportValueRow(
                label = "Pending payments",
                value = data.subscriptions.pending_payments.toString()
            )


            Spacer(
                modifier = Modifier.height(10.dp)
            )


            Box(

                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(LightGreen)
                    .padding(14.dp)
            ) {

                Row(

                    modifier = Modifier.fillMaxWidth(),

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(

                            text = "Total paid revenue",

                            color = ForestGreen,

                            fontSize = 12.sp,

                            fontWeight = FontWeight.SemiBold
                        )


                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )


                        Text(

                            text = formatIndianCurrency(
                                data.subscriptions.total_paid_revenue
                            ),

                            color = DarkGreen,

                            fontSize = 23.sp,

                            fontWeight = FontWeight.Bold
                        )
                    }


                    Icon(

                        imageVector = Icons.Default.Payments,

                        contentDescription = null,

                        tint = ForestGreen,

                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}


@Composable
private fun ReportValueRow(
    label: String,
    value: String
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(

            text = label,

            modifier = Modifier.weight(1f),

            color = TextGray,

            fontSize = 13.sp
        )


        Text(

            text = value,

            color = TextDark,

            fontSize = 13.sp,

            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
private fun BookingReportCard(
    booking: SuperAdminBooking
) {

    val normalizedStatus =
        booking.bookingStatus.uppercase(Locale.US)


    val statusColor = when (normalizedStatus) {

        "CONFIRMED",
        "COMPLETED" -> ComposeColor(0xFF237A3B)

        "CANCELLED" -> ComposeColor(0xFFC62828)

        "PENDING" -> ComposeColor(0xFFB26A00)

        else -> TextGray
    }


    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = ComposeColor.White
        ),

        border = BorderStroke(
            width = 1.dp,
            color = BorderGreen
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(

                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(LightGreen),

                    contentAlignment = Alignment.Center
                ) {

                    Icon(

                        imageVector = Icons.Default.ReceiptLong,

                        contentDescription = null,

                        tint = ForestGreen
                    )
                }


                Spacer(
                    modifier = Modifier.width(12.dp)
                )


                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(

                        text = booking.turf?.name
                            ?: "Unknown turf",

                        color = TextDark,

                        fontSize = 15.sp,

                        fontWeight = FontWeight.Bold,

                        maxLines = 1,

                        overflow = TextOverflow.Ellipsis
                    )


                    Text(

                        text = "Booking #${booking.id}",

                        color = TextGray,

                        fontSize = 12.sp
                    )
                }


                Surface(

                    shape = RoundedCornerShape(50.dp),

                    color = statusColor.copy(
                        alpha = 0.12f
                    )
                ) {

                    Text(

                        text = normalizedStatus,

                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        ),

                        color = statusColor,

                        fontSize = 10.sp,

                        fontWeight = FontWeight.Bold
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(14.dp)
            )


            HorizontalDivider(
                color = BorderGreen
            )


            Spacer(
                modifier = Modifier.height(10.dp)
            )


            BookingInfoRow(

                icon = Icons.Default.Groups,

                label = "User",

                value = booking.user?.name
                    ?: "N/A"
            )


            BookingInfoRow(

                icon = Icons.Default.LocationOn,

                label = "Location",

                value = booking.turf?.city
                    ?: "N/A"
            )


            BookingInfoRow(

                icon = Icons.Default.CalendarMonth,

                label = "Booking date",

                value = formatDateForDisplay(
                    booking.bookingDate
                )
            )


            BookingInfoRow(

                icon = Icons.Default.ReceiptLong,

                label = "Payment status",

                value = booking.paymentStatus
            )


            BookingInfoRow(

                icon = Icons.Default.Payments,

                label = "Amount",

                value = formatIndianCurrency(
                    booking.totalAmount
                )
            )
        }
    }
}


@Composable
private fun BookingInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(

            imageVector = icon,

            contentDescription = null,

            tint = ForestGreen,

            modifier = Modifier.size(17.dp)
        )


        Spacer(
            modifier = Modifier.width(9.dp)
        )


        Text(

            text = label,

            color = TextGray,

            fontSize = 12.sp,

            modifier = Modifier.width(105.dp)
        )


        Text(

            text = value,

            color = TextDark,

            fontSize = 12.sp,

            fontWeight = FontWeight.SemiBold,

            modifier = Modifier.weight(1f),

            maxLines = 2,

            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
private fun EmptyBookingsCard() {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(
            containerColor = PaleGreen
        )
    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(

                imageVector = Icons.Default.ReceiptLong,

                contentDescription = null,

                tint = ForestGreen,

                modifier = Modifier.size(40.dp)
            )


            Spacer(
                modifier = Modifier.height(10.dp)
            )


            Text(

                text = "No booking records found",

                color = TextDark,

                fontSize = 15.sp,

                fontWeight = FontWeight.Bold
            )


            Spacer(
                modifier = Modifier.height(4.dp)
            )


            Text(

                text = "Booking details will appear here when users make bookings.",

                color = TextGray,

                fontSize = 12.sp
            )
        }
    }
}


@Composable
private fun DownloadReportCard(
    onDownloadClick: () -> Unit
) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = DarkGreen
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(

                    imageVector = Icons.Default.Download,

                    contentDescription = null,

                    tint = ComposeColor.White,

                    modifier = Modifier.size(28.dp)
                )


                Spacer(
                    modifier = Modifier.width(12.dp)
                )


                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(

                        text = "Download detailed report",

                        color = ComposeColor.White,

                        fontSize = 16.sp,

                        fontWeight = FontWeight.Bold
                    )


                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )


                    Text(

                        text = "Export platform statistics and booking details as a PDF.",

                        color = ComposeColor.White.copy(
                            alpha = 0.8f
                        ),

                        fontSize = 12.sp,

                        lineHeight = 18.sp
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            Button(

                onClick = onDownloadClick,

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(14.dp),

                colors = ButtonDefaults.buttonColors(

                    containerColor = ComposeColor.White,

                    contentColor = DarkGreen
                )
            ) {

                Icon(

                    imageVector = Icons.Default.Download,

                    contentDescription = null
                )


                Spacer(
                    modifier = Modifier.width(8.dp)
                )


                Text(

                    text = "Download PDF",

                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


private fun formatIndianCurrency(
    amount: Double
): String {

    val formatter =
        java.text.NumberFormat.getCurrencyInstance(
            Locale.forLanguageTag("en-IN")
        )

    return formatter.format(amount)
}


private fun formatDateForDisplay(
    date: String?
): String {

    if (date.isNullOrBlank()) {
        return "N/A"
    }


    val inputFormats = listOf(

        "yyyy-MM-dd",

        "yyyy-MM-dd HH:mm:ss",

        "yyyy-MM-dd'T'HH:mm:ss",

        "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
    )


    for (inputFormat in inputFormats) {

        try {

            val parsedDate =
                SimpleDateFormat(
                    inputFormat,
                    Locale.US
                ).parse(date)


            if (parsedDate != null) {

                return SimpleDateFormat(
                    "dd MMM yyyy",
                    Locale.US
                ).format(parsedDate)
            }

        } catch (_: Exception) {

            // Try another date format
        }
    }


    return date
}


private fun generateDetailedReportPdf(
    context: Context,
    data: SuperAdminDashboardData,
    bookings: List<SuperAdminBooking>
) {

    val pdfDocument = PdfDocument()


    try {

        val pageWidth = 595
        val pageHeight = 842


        val titlePaint = Paint().apply {

            color = Color.rgb(
                23,
                61,
                32
            )

            textSize = 22f

            typeface = Typeface.create(
                Typeface.DEFAULT,
                Typeface.BOLD
            )
        }


        val headingPaint = Paint().apply {

            color = Color.rgb(
                46,
                107,
                53
            )

            textSize = 15f

            typeface = Typeface.create(
                Typeface.DEFAULT,
                Typeface.BOLD
            )
        }


        val normalPaint = Paint().apply {

            color = Color.DKGRAY

            textSize = 10f
        }


        val labelPaint = Paint().apply {

            color = Color.GRAY

            textSize = 9f

            typeface = Typeface.create(
                Typeface.DEFAULT,
                Typeface.BOLD
            )
        }


        var pageNumber = 1


        var page = pdfDocument.startPage(

            PdfDocument.PageInfo.Builder(
                pageWidth,
                pageHeight,
                pageNumber
            ).create()
        )


        var canvas: Canvas = page.canvas

        var yPosition = 45f


        fun startNewPage() {

            pdfDocument.finishPage(page)

            pageNumber++


            page = pdfDocument.startPage(

                PdfDocument.PageInfo.Builder(
                    pageWidth,
                    pageHeight,
                    pageNumber
                ).create()
            )


            canvas = page.canvas

            yPosition = 45f
        }


        fun checkPageSpace(
            requiredHeight: Float = 24f
        ) {

            if (
                yPosition + requiredHeight >
                pageHeight - 45f
            ) {

                startNewPage()
            }
        }


        fun drawText(
            text: String,
            paint: Paint,
            spacing: Float = 18f
        ) {

            checkPageSpace(spacing)


            canvas.drawText(
                text,
                35f,
                yPosition,
                paint
            )


            yPosition += spacing
        }


        fun drawLabelValue(
            label: String,
            value: String
        ) {

            checkPageSpace(22f)


            canvas.drawText(
                "$label:",
                35f,
                yPosition,
                labelPaint
            )


            canvas.drawText(
                value,
                170f,
                yPosition,
                normalPaint
            )


            yPosition += 19f
        }


        drawText(

            text = "BookMyTurf - Detailed Report",

            paint = titlePaint,

            spacing = 32f
        )


        drawText(

            text = "Generated on: ${
                SimpleDateFormat(
                    "dd MMM yyyy, hh:mm a",
                    Locale.US
                ).format(Date())
            }",

            paint = normalPaint,

            spacing = 28f
        )


        drawText(

            text = "Platform Summary",

            paint = headingPaint,

            spacing = 24f
        )


        drawLabelValue(
            label = "Total Users",
            value = data.users.total.toString()
        )


        drawLabelValue(
            label = "Active Users",
            value = data.users.active.toString()
        )


        drawLabelValue(
            label = "Blocked Users",
            value = data.users.blocked.toString()
        )


        drawLabelValue(
            label = "Total Admins",
            value = data.admins.total.toString()
        )


        drawLabelValue(
            label = "Active Admins",
            value = data.admins.active.toString()
        )


        drawLabelValue(
            label = "Blocked Admins",
            value = data.admins.blocked.toString()
        )


        drawLabelValue(
            label = "Total Turfs",
            value = data.turfs.total.toString()
        )


        drawLabelValue(
            label = "Total Bookings",
            value = data.bookings.total.toString()
        )


        drawLabelValue(
            label = "Total Subscriptions",
            value = data.subscriptions.total.toString()
        )


        drawLabelValue(
            label = "Active Subscriptions",
            value = data.subscriptions.active.toString()
        )


        drawLabelValue(
            label = "Paid Subscriptions",
            value = data.subscriptions.paid.toString()
        )


        drawLabelValue(
            label = "Free Trials",
            value = data.subscriptions.free_trials.toString()
        )


        drawLabelValue(
            label = "Pending Payments",
            value = data.subscriptions.pending_payments.toString()
        )


        drawLabelValue(
            label = "Total Paid Revenue",
            value = formatIndianCurrency(
                data.subscriptions.total_paid_revenue
            )
        )


        yPosition += 12f


        drawText(

            text = "Booking Details",

            paint = headingPaint,

            spacing = 25f
        )


        if (bookings.isEmpty()) {

            drawText(

                text = "No booking records available.",

                paint = normalPaint
            )

        } else {

            bookings.forEachIndexed { index, booking ->

                checkPageSpace(260f)


                drawText(

                    text = "Booking ${index + 1}",

                    paint = headingPaint,

                    spacing = 23f
                )


                drawLabelValue(

                    label = "Booking ID",

                    value = booking.id.toString()
                )


                drawLabelValue(

                    label = "User",

                    value = booking.user?.name
                        ?: "N/A"
                )


                drawLabelValue(

                    label = "User Email",

                    value = booking.user?.email
                        ?: "N/A"
                )


                drawLabelValue(

                    label = "Turf",

                    value = booking.turf?.name
                        ?: "N/A"
                )


                drawLabelValue(

                    label = "City",

                    value = booking.turf?.city
                        ?: "N/A"
                )


                drawLabelValue(

                    label = "Booking Date",

                    value = formatDateForDisplay(
                        booking.bookingDate
                    )
                )


                drawLabelValue(

                    label = "Slot",

                    value = "${
                        booking.slot?.startTime
                            ?: "N/A"
                    } - ${
                        booking.slot?.endTime
                            ?: "N/A"
                    }"
                )


                drawLabelValue(

                    label = "Amount",

                    value = formatIndianCurrency(
                        booking.totalAmount
                    )
                )


                drawLabelValue(

                    label = "Payment Status",

                    value = booking.paymentStatus
                )


                drawLabelValue(

                    label = "Booking Status",

                    value = booking.bookingStatus
                )


                drawLabelValue(

                    label = "Refund Status",

                    value = booking.refundStatus
                )


                drawLabelValue(

                    label = "Payment ID",

                    value = booking.paymentId
                        ?: "N/A"
                )


                drawLabelValue(

                    label = "Booked At",

                    value = formatDateForDisplay(
                        booking.bookedAt
                    )
                )


                yPosition += 12f
            }
        }


        pdfDocument.finishPage(page)


        val fileName =
            "BookMyTurf_Detailed_Report.pdf"


        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.Q
        ) {

            val contentValues =
                ContentValues().apply {

                    put(
                        MediaStore.Downloads.DISPLAY_NAME,
                        fileName
                    )

                    put(
                        MediaStore.Downloads.MIME_TYPE,
                        "application/pdf"
                    )

                    put(
                        MediaStore.Downloads.IS_PENDING,
                        1
                    )
                }


            val resolver =
                context.contentResolver


            val uri =
                resolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    contentValues
                )


            if (uri != null) {

                resolver.openOutputStream(uri)
                    ?.use { outputStream ->

                        pdfDocument.writeTo(
                            outputStream
                        )
                    }


                val completedValues =
                    ContentValues().apply {

                        put(
                            MediaStore.Downloads.IS_PENDING,
                            0
                        )
                    }


                resolver.update(
                    uri,
                    completedValues,
                    null,
                    null
                )


                Toast.makeText(
                    context,
                    "Detailed PDF saved in Downloads",
                    Toast.LENGTH_LONG
                ).show()

            } else {

                Toast.makeText(
                    context,
                    "Unable to create PDF file",
                    Toast.LENGTH_LONG
                ).show()
            }

        } else {

            val downloadsDirectory =
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                )


            if (!downloadsDirectory.exists()) {

                downloadsDirectory.mkdirs()
            }


            val file = File(
                downloadsDirectory,
                fileName
            )


            FileOutputStream(file).use { outputStream ->

                pdfDocument.writeTo(
                    outputStream
                )
            }


            Toast.makeText(
                context,
                "Detailed PDF saved in Downloads",
                Toast.LENGTH_LONG
            ).show()
        }

    } catch (exception: Exception) {

        Toast.makeText(
            context,
            "PDF generation failed: ${exception.message}",
            Toast.LENGTH_LONG
        ).show()

    } finally {

        pdfDocument.close()
    }
}