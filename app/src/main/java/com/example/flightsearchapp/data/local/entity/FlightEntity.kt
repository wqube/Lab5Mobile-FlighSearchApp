package com.example.flightsearchapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "flights",
    foreignKeys = [
        ForeignKey(
            entity = AirportEntity::class,
            parentColumns = ["id"],
            childColumns = ["departureAirportId"]
        ),
        ForeignKey(
            entity = AirportEntity::class,
            parentColumns = ["id"],
            childColumns = ["arrivalAirportId"]
        )
    ]
)
data class FlightEntity(
    @PrimaryKey val id: Int,
    val flightNumber: String,
    val departureAirportId: Int,
    val arrivalAirportId: Int,
    val isFavorite: Boolean = false
)