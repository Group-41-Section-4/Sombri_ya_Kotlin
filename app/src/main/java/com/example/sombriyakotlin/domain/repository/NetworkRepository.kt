package com.example.sombriyakotlin.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface NetworkRepository {
    /** Only one */
    val isConnected: StateFlow<Boolean>
}