package com.example.sombriyakotlin


import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "sombriya_prefs")
