package com.masterwok.shrimplesearch.features.query.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import com.masterwok.xamarininterface.models.IndexerQueryResult
import com.masterwok.xamarininterface.models.Query
import kotlinx.coroutines.launch
import javax.inject.Inject


class QueryViewModel @Inject constructor(
    private val jackettService: JackettService
) : ViewModel(), JackettService.Listener {

    init {
        jackettService.addListener(this)
    }

    override fun onCleared() {
        jackettService.removeListener(this)

        super.onCleared()
    }

    override fun onIndexersInitialized() = Unit

    override fun onIndexerInitialized() = Unit

    override fun onIndexerQueryResult(indexerQueryResult: IndexerQueryResult) {
        val x = 1
    }

    fun setQuery(query: Query) = viewModelScope.launch {
        jackettService.query(query)
    }

}
