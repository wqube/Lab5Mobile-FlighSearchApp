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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

class FlightRepositoryImpl(
    private val airportDao: AirportDao,
    private val flightDao: FlightDao
) : FlightRepository {

    override fun searchAirports(query: String): Flow<List<Airport>> {
        return airportDao.searchAirports(query).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getFlights(departureIata: String): Flow<List<Flight>> {
        val destinationsFlow = flightDao.getDestinationAirports(departureIata)
        val favoritesFlow = flightDao.getFavoriteFlights()

        return combine(destinationsFlow, favoritesFlow) { destinations, favorites ->
            val favSet = favorites.map { it.departure_iata to it.destination_iata }.toSet()

            destinations.map { dest ->
                Flight(
                    departureIata = departureIata,
                    departureName = "",
                    destinationIata = dest.iataCode,
                    destinationName = dest.name,
                    isFavorite = favSet.contains(departureIata to dest.iataCode)
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
        val isFav = flightDao.isFavorite(departureIata, destinationIata).first()
        if (isFav) {
            flightDao.removeFavorite(departureIata, destinationIata)
        } else {
            flightDao.addFavorite(FavoriteFlightEntity(departureIata, destinationIata))
        }
    }
}