package com.example.flightsearchapp.ui.main

import com.example.flightsearchapp.domain.model.Airport
import com.example.flightsearchapp.domain.model.Flight

data class MainUiState(
    val query: String = "",
    val airportSuggestions: List<Airport> = emptyList(),
    val flights: List<Flight> = emptyList(),
    val isShowingFavorites: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)