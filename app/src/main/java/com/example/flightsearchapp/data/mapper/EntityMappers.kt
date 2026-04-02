package com.example.flightsearchapp.data.mapper

import com.example.flightsearchapp.data.local.entity.AirportEntity
import com.example.flightsearchapp.domain.model.Airport

fun AirportEntity.toDomain(): Airport {
    return Airport(
        id = id,
        iataCode = iataCode,
        name = name
    )
}