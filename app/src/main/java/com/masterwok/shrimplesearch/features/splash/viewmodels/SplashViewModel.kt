package com.masterwok.shrimplesearch.features.splash.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import com.masterwok.shrimplesearch.features.splash.models.BootstrapInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject


class SplashViewModel @Inject constructor(
    private val jackettService: JackettService
) : ViewModel(), JackettService.Listener {

    private val _liveDataBootStrapInfo = MutableLiveData<BootstrapInfo>()
    private val _liveDataBootStrapCompleted = MutableLiveData<Unit>()

    val liveDataBoostrapInfo: LiveData<BootstrapInfo> = _liveDataBootStrapInfo
    val liveDataBootStrapCompleted: LiveData<Unit> = _liveDataBootStrapCompleted

    var count: Int = 0

    init {
        jackettService.addListener(this)
    }

    fun initialize() = viewModelScope.launch {
        _liveDataBootStrapInfo.postValue(
            BootstrapInfo(
                initializedCount = 0,
                totalIndexerCount = jackettService.getIndexerCount()
            )
        )

        jackettService.initialize()
    }

    override fun onCleared() {
        jackettService.removeListener(this)

        super.onCleared()
    }

    override fun onIndexersInitialized() {
        viewModelScope.launch {
            _liveDataBootStrapCompleted.value = Unit
        }
    }

    override fun onIndexerInitialized() {
        viewModelScope.launch {
            val newCount = checkNotNull(_liveDataBootStrapInfo.value?.initializedCount) + 1

            _liveDataBootStrapInfo.value = BootstrapInfo(
                totalIndexerCount = checkNotNull(_liveDataBootStrapInfo.value?.totalIndexerCount),
                initializedCount = newCount
            )
        }
    }

}