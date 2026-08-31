package com.example.bookmyturf.screens.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmyturf.screens.user.components.TurfCard
import com.example.bookmyturf.viewmodel.UserHomeViewModel

@Composable
fun UserHomeScreen(
    onTurfClick: (Int) -> Unit,
    viewModel: UserHomeViewModel = viewModel()
) {

    val turfs by viewModel.turfs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()


    // =========================================================
    // LOAD TURFS
    // =========================================================

    LaunchedEffect(Unit) {

        viewModel.loadTurfs()
    }


    // =========================================================
    // SCREEN
    // =========================================================

    when {

        // =====================================================
        // LOADING
        // =====================================================

        isLoading -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()
            }
        }


        // =====================================================
        // ERROR
        // =====================================================

        error != null -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = error
                        ?: "Something went wrong.",
                    fontSize = 18.sp
                )
            }
        }


        // =====================================================
        // EMPTY
        // =====================================================

        turfs.isEmpty() -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "No turfs available.",
                    fontSize = 18.sp
                )
            }
        }


        // =====================================================
        // TURF LIST
        // =====================================================

        else -> {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),

                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = turfs,
                    key = { turf ->
                        turf.id
                    }
                ) { turf ->

                    TurfCard(
                        turf = turf,

                        onClick = {
                            onTurfClick(turf.id)
                        },

                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}