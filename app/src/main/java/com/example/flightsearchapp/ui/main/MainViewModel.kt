package com.example.flightsearchapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearchapp.data.datastore.SearchPreferences
import com.example.flightsearchapp.domain.model.Airport
import com.example.flightsearchapp.domain.model.Flight
import com.example.flightsearchapp.domain.repository.FlightRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: FlightRepository,
    private val preferences: SearchPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private var listJob: Job? = null

    init {
        viewModelScope.launch {
            val savedIata = preferences.lastIataFlow().first()
            if (savedIata.isNullOrEmpty()) {
                loadFavorites()
            } else {
                _uiState.update { it.copy(query = savedIata) }
                subscribeToFlights(savedIata)
            }
        }
    }

    private fun subscribeToFlights(departureIata: String) {
        listJob?.cancel()
        listJob = repository.getFlights(departureIata)
            .onEach { flights ->
                _uiState.update {
                    it.copy(
                        flights = flights,
                        isShowingFavorites = false,
                        isLoading = false,
                        error = null
                    )
                }
            }
            .catch { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Ошибка загрузки рейсов: ${e.localizedMessage}"
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun subscribeToFavorites() {
        listJob?.cancel()
        listJob = repository.getFavorites()
            .onEach { flights ->
                _uiState.update {
                    it.copy(
                        flights = flights,
                        isShowingFavorites = true,
                        isLoading = false,
                        error = null
                    )
                }
            }
            .catch { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Ошибка загрузки избранного: ${e.localizedMessage}"
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onQueryChanged(text: String) {
        viewModelScope.launch {
            if (text.isBlank()) {
                preferences.clear()
                _uiState.update {
                    it.copy(
                        query = "",
                        airportSuggestions = emptyList(),
                        flights = emptyList(),
                        error = null
                    )
                }
                loadFavorites()
            } else {
                _uiState.update { it.copy(query = text, error = null) }
                try {
                    val airports = repository.searchAirports(text).first()
                    _uiState.update {
                        it.copy(
                            airportSuggestions = airports,
                            flights = emptyList(),
                            isShowingFavorites = false
                        )
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(error = "Ошибка поиска: ${e.localizedMessage}")
                    }
                }
            }
        }
    }

    fun onAirportSelected(airport: Airport) {
        viewModelScope.launch {
            preferences.saveLastIata(airport.iataCode)
        }

        _uiState.update {
            it.copy(
                query = airport.iataCode,
                airportSuggestions = emptyList(),
                isShowingFavorites = false,
                isLoading = true
            )
        }
        subscribeToFlights(airport.iataCode)
    }

    fun loadFavorites() {
        subscribeToFavorites()
    }

    fun toggleFavorite(flight: Flight) {
        viewModelScope.launch {
            try {
                repository.toggleFavorite(
                    departureIata = flight.departureIata,
                    destinationIata = flight.destinationIata
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Ошибка обновления избранного: ${e.localizedMessage}")
                }
            }
        }
    }
}