package com.example.flightsearchapp.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearchapp.domain.model.Flight


@Composable
fun MainScreen() {

    val context = LocalContext.current

    val viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(context)
    )

    val state by viewModel.uiState.collectAsState()

    MainScreenContent(
        state = state,
        onQueryChange = viewModel::onQueryChanged,
        onAirportSelected = viewModel::onAirportSelected,
        onFavoriteClick = { flight: Flight -> viewModel.toggleFavorite(flight) }
    )
}