package com.example.flightsearchapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearchapp.data.datastore.SearchPreferences
import com.example.flightsearchapp.domain.model.Airport
import com.example.flightsearchapp.domain.model.Flight
import com.example.flightsearchapp.domain.repository.FlightRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: FlightRepository,
    private val preferences: SearchPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        viewModelScope.launch {
            preferences.lastIataFlow().collect { iata ->
                if (iata.isNullOrEmpty()) {
                    loadFavorites()
                } else {
                    searchByIata(iata)
                }
            }
        }
    }

    private fun searchByIata(iata: String) {
        viewModelScope.launch {
            val airports = repository.searchAirports(iata)
            val airport = airports.firstOrNull() ?: return@launch

            repository.getFlights(airport.iataCode)  // ← было airport.id
                .onEach { flights ->
                    _uiState.update {
                        it.copy(
                            query = iata,
                            flights = flights,
                            isShowingFavorites = false
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    fun onQueryChanged(text: String) {
        viewModelScope.launch {
            val airports = repository.searchAirports(text)
            _uiState.update {
                it.copy(
                    query = text,
                    airportSuggestions = airports,
                    flights = emptyList()
                )
            }
        }
    }

    fun onAirportSelected(airport: Airport) {
        viewModelScope.launch {
            preferences.saveLastIata(airport.iataCode)
        }

        repository.getFlights(airport.iataCode)  // ← было airport.id
            .onEach { flights ->
                _uiState.update {
                    it.copy(
                        airportSuggestions = emptyList(),
                        flights = flights,
                        isShowingFavorites = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun loadFavorites() {
        repository.getFavorites()
            .onEach { flights ->
                _uiState.update {
                    it.copy(
                        flights = flights,
                        isShowingFavorites = true
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun toggleFavorite(flight: Flight) {
        viewModelScope.launch {
            repository.toggleFavorite(
                departureIata = flight.departureIata,
                destinationIata = flight.destinationIata
            )
        }
    }
}