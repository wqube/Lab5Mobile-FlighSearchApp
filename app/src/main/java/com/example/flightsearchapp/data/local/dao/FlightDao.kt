package com.example.flightsearchapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flightsearchapp.data.local.entity.AirportEntity
import com.example.flightsearchapp.data.local.entity.FavoriteFlightEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {

    // Все аэропорты кроме выбранного — это и есть список рейсов
    @Query("SELECT * FROM airport WHERE iata_code != :departureIata")
    fun getDestinationAirports(departureIata: String): Flow<List<AirportEntity>>

    @Query("SELECT * FROM favorite_flight")
    fun getFavoriteFlights(): Flow<List<FavoriteFlightEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(flight: FavoriteFlightEntity)

    @Query("DELETE FROM favorite_flight WHERE departure_iata = :depIata AND destination_iata = :destIata")
    suspend fun removeFavorite(depIata: String, destIata: String)

    @Query("SELECT COUNT(*) FROM favorite_flight WHERE departure_iata = :depIata AND destination_iata = :destIata")
    suspend fun isFavorite(depIata: String, destIata: String): Int
}