package com.example.flightsearchapp.data.local.entity

import androidx.room.Entity

@Entity(tableName = "favorite_flight", primaryKeys = ["departure_iata", "destination_iata"])
data class FavoriteFlightEntity(
    val departure_iata: String,
    val destination_iata: String
)