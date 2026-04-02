package com.example.flightsearchapp.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flightsearchapp.domain.model.Airport
import com.example.flightsearchapp.domain.model.Flight

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

        if (state.error != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return
        }

        if (state.airportSuggestions.isNotEmpty()) {
            AirportSuggestions(
                airports = state.airportSuggestions,
                onAirportSelected = onAirportSelected
            )
            return
        }

        if (state.isShowingFavorites) {
            Text(
                text = "Избранные рейсы",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        } else if (state.query.isNotBlank()) {
            Text(
                text = "Рейсы из ${state.query}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (state.flights.isNotEmpty()) {
            FlightsList(
                flights = state.flights,
                onFavoriteClick = onFavoriteClick
            )
        } else if (state.error == null) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (state.isShowingFavorites)
                        "Нет избранных рейсов"
                    else
                        "Рейсы не найдены",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}