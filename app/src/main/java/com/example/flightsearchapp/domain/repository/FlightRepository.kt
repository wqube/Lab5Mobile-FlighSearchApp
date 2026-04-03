package com.example.flightsearchapp.domain.repository

import com.example.flightsearchapp.domain.model.Airport
import com.example.flightsearchapp.domain.model.Flight
import kotlinx.coroutines.flow.Flow

interface FlightRepository {
    fun searchAirports(query: String): Flow<List<Airport>>
    fun getFlights(departureIata: String): Flow<List<Flight>>
    fun getFavorites(): Flow<List<Flight>>
    suspend fun toggleFavorite(departureIata: String, destinationIata: String)
}