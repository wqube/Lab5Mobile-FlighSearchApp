package com.example.flightsearchapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "search_prefs")

class SearchPreferences(private val context: Context) {

    companion object {
        val LAST_IATA = stringPreferencesKey("last_iata")
    }

    fun lastIataFlow(): Flow<String?> {
        return context.dataStore.data
            .map { prefs -> prefs[LAST_IATA] }
    }

    suspend fun saveLastIata(iata: String) {
        context.dataStore.edit { prefs ->
            prefs[LAST_IATA] = iata
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}