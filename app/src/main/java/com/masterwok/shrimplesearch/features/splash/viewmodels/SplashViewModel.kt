package com.masterwok.shrimplesearch.features.splash.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import com.masterwok.shrimplesearch.features.splash.models.BootstrapInfo
import kotlinx.coroutines.launch
import javax.inject.Inject


class SplashViewModel @Inject constructor(
    private val jackettService: JackettService
) : ViewModel(), JackettService.Listener {

    private val _liveDataBootStrapInfo = MutableLiveData<BootstrapInfo>()
    private val _liveDataBootStrapCompleted = MutableLiveData<Unit>()

    val liveDataBoostrapInfo: LiveData<BootstrapInfo> = _liveDataBootStrapInfo
    val liveDataBootStrapCompleted: LiveData<Unit> = _liveDataBootStrapCompleted

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

    override fun onIndexersInitialized() = _liveDataBootStrapCompleted.postValue(Unit)

    override fun OnIndexerInitialized() {
        viewModelScope.launch {
            val currentBootstrapInfo = _liveDataBootStrapInfo.value

            val nextBootstrapInfo = currentBootstrapInfo
                ?.copy(initializedCount = currentBootstrapInfo.initializedCount + 1)
                ?: BootstrapInfo(
                    initializedCount = 1,
                    totalIndexerCount = jackettService.getIndexerCount()
                )

            _liveDataBootStrapInfo.postValue(nextBootstrapInfo)
        }
    }

}