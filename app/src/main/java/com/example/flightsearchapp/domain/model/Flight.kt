package com.example.flightsearchapp.domain.model

data class Flight(
    val departureIata: String,
    val departureName: String,
    val destinationIata: String,
    val destinationName: String,
    val isFavorite: Boolean = false
)