package com.example.sombriyakotlin

import android.app.Application
import com.example.sombriyakotlin.data.location.GPSManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SombriYaKotlinApp : Application(){
    override fun onCreate() {
        super.onCreate()
        GPSManager.initialize(this)
    }
}
