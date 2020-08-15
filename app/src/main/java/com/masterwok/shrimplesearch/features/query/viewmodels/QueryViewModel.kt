package com.masterwok.shrimplesearch.features.query.viewmodels

import androidx.lifecycle.*
import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import com.masterwok.shrimplesearch.features.query.enums.QueryState
import com.masterwok.xamarininterface.models.Indexer
import com.masterwok.xamarininterface.models.IndexerQueryResult
import com.masterwok.xamarininterface.models.Query
import com.masterwok.xamarininterface.models.QueryResultItem
import kotlinx.coroutines.launch
import javax.inject.Inject


class QueryViewModel @Inject constructor(
    private val jackettService: JackettService
) : ViewModel(), JackettService.Listener {

    private val _liveDataIndexerQueryResults = MutableLiveData<MutableList<IndexerQueryResult>>(
        mutableListOf()
    )

    private val _liveDataQueryState = MutableLiveData<QueryState?>(null)
    private val _liveDataQueryCompleted = MutableLiveData<Unit>()
    private val _liveDataSelectedIndexer = MutableLiveData<Indexer>()

    val liveDataIndexerQueryResults: LiveData<List<IndexerQueryResult>> =
        _liveDataIndexerQueryResults.map { it.toList() }

    val liveDataQueryCompleted: LiveData<Unit> = _liveDataQueryCompleted

    val liveDataQueryState = _liveDataQueryState

    var liveDataSelectedIndexerQueryResultItem =
        _liveDataIndexerQueryResults.map { indexerQueryResults ->

            val selectedIndexer = _liveDataSelectedIndexer.value
                ?: return@map emptyList<QueryResultItem>()

            val indexerQueryResult = indexerQueryResults.firstOrNull {
                it.indexer.id == selectedIndexer.id
            }

            val items = indexerQueryResult
                ?.items
                ?: indexerQueryResults.flatMap { it.items }

            return@map items.sortedByDescending { it.statInfo.seeders }
        }

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

            _liveDataIndexerQueryResults.value = results
        }
    }

    override fun onQueryCompleted() {
        viewModelScope.launch {
            _liveDataQueryCompleted.value = Unit
            _liveDataQueryState.value = QueryState.Completed
        }
    }

    fun setQuery(query: Query) = viewModelScope.launch {
        _liveDataIndexerQueryResults.value?.clear()
        _liveDataSelectedIndexer.value = null
        _liveDataQueryState.value = QueryState.Pending
        jackettService.query(query)
    }

    fun setSelectedIndexer(indexer: Indexer) {
        _liveDataSelectedIndexer.postValue(indexer)
    }

}
