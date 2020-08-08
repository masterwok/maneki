package com.masterwok.shrimplesearch.features.query.viewmodels

import androidx.lifecycle.*
import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import com.masterwok.xamarininterface.models.IndexerQueryResult
import com.masterwok.xamarininterface.models.Query
import kotlinx.coroutines.launch
import javax.inject.Inject


class QueryViewModel @Inject constructor(
    private val jackettService: JackettService
) : ViewModel(), JackettService.Listener {

    private val _liveDataIndexerQueryResults = MutableLiveData<MutableList<IndexerQueryResult>>(
        mutableListOf()
    )

    private val _liveDataQueryCompleted = MutableLiveData<Unit>()

    val liveDataIndexerQueryResults: LiveData<List<IndexerQueryResult>> =
        _liveDataIndexerQueryResults.map { it.toList() }

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
            val results = checkNotNull(_liveDataIndexerQueryResults.value)

            results.add(indexerQueryResult)
            results.sortByDescending { it.magnetCount }

            _liveDataIndexerQueryResults.value = results
        }
    }

    override fun onQueryCompleted() {
        viewModelScope.launch {
            _liveDataQueryCompleted.value = Unit
        }
    }

    fun setQuery(query: Query) = viewModelScope.launch {
        checkNotNull(_liveDataIndexerQueryResults.value).clear()
        jackettService.query(query)
    }

}
