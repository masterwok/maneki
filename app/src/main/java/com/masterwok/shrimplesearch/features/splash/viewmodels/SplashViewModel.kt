package com.masterwok.shrimplesearch.features.splash.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import kotlinx.coroutines.launch
import javax.inject.Inject


class SplashViewModel @Inject constructor(
    private val jackettService: JackettService
) : ViewModel(), JackettService.Listener {

    init {
        jackettService.addListener(this)
    }

    fun initialize() = viewModelScope.launch {
        jackettService.initialize()
    }

    override fun onCleared() {
        jackettService.removeListener(this)

        super.onCleared()
    }

    override fun onIndexersInitialized() {
        viewModelScope.launch {
            val indexerCount = jackettService.getIndexerCount()
        }
    }

    override fun OnIndexerInitialized() {
        viewModelScope.launch {
            val indexerCount = jackettService.getIndexerCount()
        }
    }

}