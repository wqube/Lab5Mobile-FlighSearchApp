package com.example.flightsearchapp.ui.main

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.flightsearchapp.domain.model.Flight

@Composable
fun FlightsList(
    flights: List<Flight>,
    onFavoriteClick: (Flight) -> Unit
) {
    LazyColumn {
        items(flights) { flight ->
            FlightItem(
                flight = flight,
                onFavoriteClick = { onFavoriteClick(flight) }
            )
        }
    }
}