package com.masterwok.shrimplesearch.features.query.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _liveDataIndexerQueryResults = MutableLiveData<List<IndexerQueryResult>>(
        emptyList()
    )

    private val _liveDataQueryCompleted = MutableLiveData<Unit>()

    val liveDataIndexerQueryResults: LiveData<List<IndexerQueryResult>> =
        _liveDataIndexerQueryResults

    val liveDataQueryCompleted: LiveData<Unit> = _liveDataQueryCompleted;

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
        viewModelScope.launch {
            _liveDataIndexerQueryResults.value =
                (_liveDataIndexerQueryResults.value ?: emptyList()) + listOf(indexerQueryResult)
        }
    }

    override fun onQueryCompleted() {
        viewModelScope.launch {
            _liveDataQueryCompleted.value = Unit
        }
    }

    fun setQuery(query: Query) = viewModelScope.launch {
        _liveDataIndexerQueryResults.value = emptyList()
        jackettService.query(query)
    }

}
