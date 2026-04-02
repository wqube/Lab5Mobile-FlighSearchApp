package com.example.flightsearchapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.flightsearchapp.data.local.entity.AirportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Query("""
        SELECT * FROM airport
        WHERE iata_code LIKE '%' || :query || '%'
           OR name LIKE '%' || :query || '%'
        ORDER BY passengers DESC
        LIMIT 10
    """)
    fun searchAirports(query: String): Flow<List<AirportEntity>>
}