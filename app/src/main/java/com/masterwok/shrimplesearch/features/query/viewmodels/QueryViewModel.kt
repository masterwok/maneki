package com.masterwok.shrimplesearch.features.query.viewmodels

import androidx.lifecycle.*
import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import com.masterwok.shrimplesearch.features.query.constants.IndexerQueryResultSortBy
import com.masterwok.shrimplesearch.features.query.constants.OrderBy
import com.masterwok.shrimplesearch.features.query.constants.QueryState
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

    private val _liveDataSort = MutableLiveData<Pair<IndexerQueryResultSortBy, OrderBy>>(
        IndexerQueryResultSortBy.Leechers to OrderBy.Descending
    )

    val liveDataSort: LiveData<Pair<IndexerQueryResultSortBy, OrderBy>> = _liveDataSort

    val liveDataIndexerQueryResults: LiveData<List<IndexerQueryResult>> =
        _liveDataIndexerQueryResults.map { it.toList() }

    val liveDataQueryCompleted: LiveData<Unit> = _liveDataQueryCompleted

    val liveDataQueryState = _liveDataQueryState

    val liveDataSelectedIndexerQueryResultItem = MediatorLiveData<List<QueryResultItem>>().apply {
        addSource(_liveDataSort) { value = getSelectedIndexerQueryResults() }
        addSource(_liveDataIndexerQueryResults) { value = getSelectedIndexerQueryResults() }
        addSource(_liveDataSelectedIndexer) { value = getSelectedIndexerQueryResults() }
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
        _liveDataIndexerQueryResults.postValue(mutableListOf())
        _liveDataSelectedIndexer.postValue(null)
        _liveDataQueryState.postValue(QueryState.Pending)
        jackettService.query(query)
    }

    fun setSort(
        sort: IndexerQueryResultSortBy,
        orderBy: OrderBy
    ) = _liveDataSort.postValue(sort to orderBy)

    fun setSelectedIndexer(indexer: Indexer) {
        _liveDataSelectedIndexer.postValue(indexer)
    }

    private fun getSelectedIndexerQueryResults(): List<QueryResultItem> {
        val queryResults = _liveDataIndexerQueryResults.value
            ?: emptyList<IndexerQueryResult>()

        val selectedIndexer = _liveDataSelectedIndexer.value
            ?: return emptyList()

        val indexerQueryResult = queryResults.firstOrNull {
            it.indexer.id == selectedIndexer.id
        }

        val items = indexerQueryResult
            ?.items
            ?: (indexerQueryResult?.items ?: queryResults.flatMap { it.items })

        return items.sortedByDescending { it.statInfo.seeders }
    }

}
