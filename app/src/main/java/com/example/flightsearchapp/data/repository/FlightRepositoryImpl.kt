package com.example.flightsearchapp.data.repository

import com.example.flightsearchapp.data.local.dao.AirportDao
import com.example.flightsearchapp.data.local.dao.FlightDao
import com.example.flightsearchapp.data.local.entity.FavoriteFlightEntity
import com.example.flightsearchapp.domain.repository.FlightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.flightsearchapp.domain.model.Airport
import com.example.flightsearchapp.domain.model.Flight
import com.example.flightsearchapp.data.mapper.toDomain

class FlightRepositoryImpl(
    private val airportDao: AirportDao,
    private val flightDao: FlightDao
) : FlightRepository {

    override suspend fun searchAirports(query: String): List<Airport> {
        return airportDao.searchAirports(query).map { it.toDomain() }
    }

    override fun getFlights(departureIata: String): Flow<List<Flight>> {
        return flightDao.getDestinationAirports(departureIata)
            .map { destinations ->
                destinations.map { dest ->
                    Flight(
                        departureIata = departureIata,
                        departureName = "",
                        destinationIata = dest.iataCode,
                        destinationName = dest.name,
                        isFavorite = false
                    )
                }
            }
    }

    override fun getFavorites(): Flow<List<Flight>> {
        return flightDao.getFavoriteFlights()
            .map { favorites ->
                favorites.map { fav ->
                    Flight(
                        departureIata = fav.departure_iata,
                        departureName = "",
                        destinationIata = fav.destination_iata,
                        destinationName = "",
                        isFavorite = true
                    )
                }
            }
    }

    override suspend fun toggleFavorite(departureIata: String, destinationIata: String) {
        val count = flightDao.isFavorite(departureIata, destinationIata)
        if (count > 0) {
            flightDao.removeFavorite(departureIata, destinationIata)
        } else {
            flightDao.addFavorite(FavoriteFlightEntity(departureIata, destinationIata))
        }
    }
}