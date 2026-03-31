package com.example.flightsearchapp.domain.repository

import com.example.flightsearchapp.domain.model.Airport
import com.example.flightsearchapp.domain.model.Flight
import kotlinx.coroutines.flow.Flow

interface FlightRepository {
    suspend fun searchAirports(query: String): List<Airport>
    fun getFlights(departureIata: String): Flow<List<Flight>>
    fun getFavorites(): Flow<List<Flight>>
    suspend fun toggleFavorite(departureIata: String, destinationIata: String)
}