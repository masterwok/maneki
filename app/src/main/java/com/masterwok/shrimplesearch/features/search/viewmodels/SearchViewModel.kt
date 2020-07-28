package com.masterwok.shrimplesearch.features.search.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val jackettService: JackettService
) : ViewModel() {

    fun initialize() = viewModelScope.launch {
        if (!jackettService.isInitialized) {
            jackettService.initialize()
        }
    }
}
