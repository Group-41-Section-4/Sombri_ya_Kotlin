package com.example.sombriyakotlin.data.network

import com.example.sombriyakotlin.ui.di.ApplicationScope
import com.example.sombriyakotlin.domain.repository.NetworkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepositoryImpl @Inject constructor(
    private val networkObserver: NetworkObserver,
    @ApplicationScope private val appScope: CoroutineScope
) : NetworkRepository  {

    override val isConnected: StateFlow<Boolean> =
        networkObserver.observe()
            .distinctUntilChanged()
            .onStart { emit(networkObserver.isConnectedNow()) }
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = appScope,
                started = kotlinx.coroutines.flow.SharingStarted.Eagerly,
                initialValue = networkObserver.isConnectedNow()
            )
}