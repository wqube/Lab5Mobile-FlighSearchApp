package com.example.flightsearchapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.flightsearchapp.data.local.entity.AirportEntity

@Dao
interface AirportDao {

    @Query("""
        SELECT * FROM airport
        WHERE iata_code LIKE '%' || :query || '%'
           OR name LIKE '%' || :query || '%'
        LIMIT 10
    """)
    suspend fun searchAirports(query: String): List<AirportEntity>
}