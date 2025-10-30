package com.example.sombriyakotlin.ui.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

fun isOnline(context: Context): Boolean {
    val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}
