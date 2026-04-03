package com.example.flightsearchapp.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flightsearchapp.domain.model.Airport

@Composable
fun AirportSuggestions(
    airports: List<Airport>,
    onAirportSelected: (Airport) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(airports) { airport ->
            Text(
                text = "${airport.iataCode} — ${airport.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAirportSelected(airport) }
                    .padding(12.dp)
            )
            Divider()
        }
    }
}