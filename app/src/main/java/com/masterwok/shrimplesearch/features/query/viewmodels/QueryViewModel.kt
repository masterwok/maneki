package com.masterwok.shrimplesearch.features.query.viewmodels

import androidx.lifecycle.*
import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import com.masterwok.shrimplesearch.features.query.constants.IndexerQueryResultSortBy
import com.masterwok.shrimplesearch.features.query.constants.OrderBy
import com.masterwok.shrimplesearch.features.query.constants.QueryResultSortBy
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

    private val _liveDataSortQueryResults =
        MutableLiveData(QueryResultSortBy.MagnetCount to OrderBy.Descending)

    private val _liveDataSortIndexerQueryResults =
        MutableLiveData(IndexerQueryResultSortBy.Seeders to OrderBy.Descending)

    val liveDataSortQueryResults: LiveData<Pair<QueryResultSortBy, OrderBy>> =
        _liveDataSortQueryResults

    val liveDataSortIndexerQueryResults: LiveData<Pair<IndexerQueryResultSortBy, OrderBy>> =
        _liveDataSortIndexerQueryResults

    val liveDataQueryCompleted: LiveData<Unit> = _liveDataQueryCompleted

    val liveDataQueryState = _liveDataQueryState

    val liveDataSelectedIndexerQueryResultItem = MediatorLiveData<List<QueryResultItem>>().apply {
        addSource(_liveDataSortIndexerQueryResults) { value = getSelectedIndexerQueryResults() }
        addSource(_liveDataIndexerQueryResults) { value = getSelectedIndexerQueryResults() }
        addSource(_liveDataSelectedIndexer) { value = getSelectedIndexerQueryResults() }
    }

    val liveDataQueryResults = MediatorLiveData<List<IndexerQueryResult>>().apply {
        addSource(_liveDataSortQueryResults) { value = getIndexerQueryResults() }
        addSource(_liveDataIndexerQueryResults) { value = getIndexerQueryResults() }
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

    fun setSortQueryResults(
        sort: QueryResultSortBy,
        orderBy: OrderBy
    ) = _liveDataSortQueryResults.postValue(sort to orderBy)

    fun setSortQueryResultItems(
        sort: IndexerQueryResultSortBy,
        orderBy: OrderBy
    ) = _liveDataSortIndexerQueryResults.postValue(sort to orderBy)

    fun setSelectedIndexer(indexer: Indexer) {
        _liveDataSelectedIndexer.postValue(indexer)
    }

    private fun getIndexerQueryResults(): List<IndexerQueryResult> {
        val queryResults = _liveDataIndexerQueryResults.value
            ?: emptyList()

        val sortValue = checkNotNull(_liveDataSortQueryResults.value)

        return sortIndexers(
            queryResults,
            sortValue.first,
            sortValue.second
        )
    }

    private fun getSelectedIndexerQueryResults(): List<QueryResultItem> {
        val queryResults = _liveDataIndexerQueryResults.value
            ?: emptyList()

        val selectedIndexer = _liveDataSelectedIndexer.value
            ?: return emptyList()

        val indexerQueryResult = queryResults.firstOrNull {
            it.indexer.id == selectedIndexer.id
        }

        val items = indexerQueryResult
            ?.items
            ?: (indexerQueryResult?.items ?: queryResults.flatMap { it.items })

        val sortValue = checkNotNull(_liveDataSortIndexerQueryResults.value)

        return sortQueryResultItems(
            items,
            sortValue.first,
            sortValue.second
        )
    }

    private fun sortIndexers(
        results: List<IndexerQueryResult>,
        sortBy: QueryResultSortBy,
        orderBy: OrderBy
    ): List<IndexerQueryResult> = when (sortBy) {
        QueryResultSortBy.Name -> when (orderBy) {
            OrderBy.Ascending -> results.sortedBy { it.indexer.displayName }
            OrderBy.Descending -> results.sortedByDescending { it.indexer.displayName }
        }
        QueryResultSortBy.MagnetCount -> when (orderBy) {
            OrderBy.Ascending -> results.sortedBy { it.magnetCount }
            OrderBy.Descending -> results.sortedByDescending { it.magnetCount }
        }
        QueryResultSortBy.LinkCount -> when (orderBy) {
            OrderBy.Ascending -> results.sortedBy { it.linkCount }
            OrderBy.Descending -> results.sortedByDescending { it.linkCount }
        }
        QueryResultSortBy.AggregateCount -> when (orderBy) {
            OrderBy.Ascending -> results.sortedBy { it.linkCount + it.magnetCount }
            OrderBy.Descending -> results.sortedByDescending { it.linkCount + it.magnetCount }
        }
    }

    private fun sortQueryResultItems(
        items: List<QueryResultItem>,
        sortBy: IndexerQueryResultSortBy,
        orderBy: OrderBy
    ): List<QueryResultItem> = when (sortBy) {
        IndexerQueryResultSortBy.Name -> when (orderBy) {
            OrderBy.Ascending -> items.sortedBy { it.title }
            OrderBy.Descending -> items.sortedByDescending { it.title }
        }
        IndexerQueryResultSortBy.Peers -> when (orderBy) {
            OrderBy.Ascending -> items.sortedBy { it.statInfo.peers }
            OrderBy.Descending -> items.sortedByDescending { it.statInfo.peers }
        }
        IndexerQueryResultSortBy.Seeders -> when (orderBy) {
            OrderBy.Ascending -> items.sortedBy { it.statInfo.seeders }
            OrderBy.Descending -> items.sortedByDescending { it.statInfo.seeders }
        }
        IndexerQueryResultSortBy.Size -> when (orderBy) {
            OrderBy.Ascending -> items.sortedBy { it.statInfo.size }
            OrderBy.Descending -> items.sortedByDescending { it.statInfo.size }
        }
        IndexerQueryResultSortBy.PublishedOn -> when (orderBy) {
            OrderBy.Ascending -> items.sortedBy { it.statInfo.publishedOn }
            OrderBy.Descending -> items.sortedByDescending { it.statInfo.publishedOn }
        }
    }

}
