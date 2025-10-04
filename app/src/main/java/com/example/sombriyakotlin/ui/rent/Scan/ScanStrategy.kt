package com.example.sombriyakotlin.feature.rent.Scan

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable

interface ScanStrategy {
    fun start(activity: Activity)
    fun stop(activity: Activity)
    @Composable()
    fun render()
    fun onNewIntent(intent: Intent)
}