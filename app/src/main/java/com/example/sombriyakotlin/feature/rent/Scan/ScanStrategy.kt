package com.example.sombriyakotlin.feature.rent.Scan

import android.app.Activity
import androidx.compose.runtime.Composable

interface ScanStrategy {
    fun start(activity: Activity)
    fun stop(activity: Activity)

    @Composable
    fun Render()   // How looks startegy on UI
}