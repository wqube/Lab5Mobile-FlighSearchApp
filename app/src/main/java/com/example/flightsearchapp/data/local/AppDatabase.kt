package com.example.flightsearchapp.data.local

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flightsearchapp.data.local.dao.AirportDao
import com.example.flightsearchapp.data.local.dao.FlightDao
import com.example.flightsearchapp.data.local.entity.AirportEntity
import com.example.flightsearchapp.data.local.entity.FavoriteFlightEntity

@Database(
    entities = [AirportEntity::class, FavoriteFlightEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun airportDao(): AirportDao
    abstract fun flightDao(): FlightDao

    companion object {
        fun create(context: Context): AppDatabase =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "flight_search.db"
            )
            .createFromAsset("flight_search.db")
            .build()
    }
}