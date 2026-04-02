package com.example.flightsearchapp.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flightsearchapp.domain.model.Airport
import com.example.flightsearchapp.domain.model.Flight
import kotlin.collections.isNotEmpty

@Composable
fun MainScreenContent(
    state: MainUiState,
    onQueryChange: (String) -> Unit,
    onAirportSelected: (Airport) -> Unit,
    onFavoriteClick: (Flight) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        SearchBar(
            query = state.query,
            onQueryChange = onQueryChange
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state.airportSuggestions.isNotEmpty()) {
            AirportSuggestions(
                airports = state.airportSuggestions,
                onAirportSelected = onAirportSelected
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Показываем подсказки, если они есть
        if (state.airportSuggestions.isNotEmpty()) {
            AirportSuggestions(
                airports = state.airportSuggestions,
                onAirportSelected = onAirportSelected
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Показываем список рейсов, если есть
        if (state.flights.isNotEmpty()) {
            FlightsList(
                flights = state.flights,
                onFavoriteClick = onFavoriteClick
            )
        } else if (state.airportSuggestions.isEmpty()) {
            // Показать "No flights found" только если подсказок нет
            Text("No flights found")
        }
    }
}