package com.example.flightsearchapp.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flightsearchapp.data.datastore.SearchPreferences
import com.example.flightsearchapp.data.local.AppDatabase
import com.example.flightsearchapp.data.repository.FlightRepositoryImpl

class MainViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val database = AppDatabase.create(context.applicationContext)

        val repository = FlightRepositoryImpl(
            airportDao = database.airportDao(),
            flightDao = database.flightDao()
        )

        val preferences = SearchPreferences(context)

        return MainViewModel(
            repository = repository,
            preferences = preferences
        ) as T
    }
}