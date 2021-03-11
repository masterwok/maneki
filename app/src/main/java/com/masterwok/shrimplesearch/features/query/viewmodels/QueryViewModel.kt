package com.masterwok.shrimplesearch.features.query.viewmodels

import androidx.lifecycle.*
import com.masterwok.shrimplesearch.common.constants.AnalyticEvent
import com.masterwok.shrimplesearch.common.data.models.UserSettings
import com.masterwok.shrimplesearch.common.data.repositories.contracts.ConfigurationRepository
import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import com.masterwok.shrimplesearch.common.data.repositories.contracts.UserSettingsRepository
import com.masterwok.shrimplesearch.common.data.services.contracts.AnalyticService
import com.masterwok.shrimplesearch.di.modules.RepositoryModule
import com.masterwok.shrimplesearch.features.query.constants.IndexerQueryResultSortBy
import com.masterwok.shrimplesearch.features.query.constants.OrderBy
import com.masterwok.shrimplesearch.features.query.constants.QueryResultSortBy
import com.masterwok.xamarininterface.enums.QueryState
import com.masterwok.xamarininterface.models.Indexer
import com.masterwok.xamarininterface.models.IndexerQueryResult
import com.masterwok.xamarininterface.models.Query
import com.masterwok.xamarininterface.models.QueryResultItem
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


class QueryViewModel @Inject constructor(
    private val jackettService: JackettService,
    private val userSettingsRepository: UserSettingsRepository,
    private val analyticService: AnalyticService,
    private val configurationRepository: ConfigurationRepository,
    @Named(RepositoryModule.NAMED_IN_APP_REVIEW_RESULT_ITEM_TAP_COUNT) private val inAppReviewResultItemTapCount: Int
) : ViewModel(), JackettService.Listener {

    private val _liveDataIndexerQueryResults = MutableLiveData(
        jackettService.queryResults.toMutableList()
    )

    private val _liveDataQueryState = MutableLiveData(jackettService.queryState)
    private val _liveDataSelectedIndexer = MutableLiveData<Indexer>()

    private val _liveDataSortQueryResults =
        MutableLiveData(QueryResultSortBy.MagnetCount to OrderBy.Descending)

    private val _liveDataSortIndexerQueryResults =
        MutableLiveData(IndexerQueryResultSortBy.Seeders to OrderBy.Descending)

    val liveDataSortQueryResults: LiveData<Pair<QueryResultSortBy, OrderBy>> =
        _liveDataSortQueryResults

    val liveDataSortIndexerQueryResults: LiveData<Pair<IndexerQueryResultSortBy, OrderBy>> =
        _liveDataSortIndexerQueryResults

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

    suspend fun shouldAttemptToPresentInAppReview(): Boolean {
        val count = configurationRepository.getResultItemTapCount()

        return if (count == 0L) false else count % inAppReviewResultItemTapCount == 0L
    }

    suspend fun incrementResultItemTapCount() = configurationRepository.incrementResultTapCount()

    init {
        jackettService.addListener(this)
    }

    override fun onCleared() {
        jackettService.removeListener(this@QueryViewModel)

        super.onCleared()
    }

    override fun onIndexersInitialized() = Unit

    override fun onIndexerInitialized() = Unit

    override fun onResultsUpdated() {
        viewModelScope.launch {
            _liveDataIndexerQueryResults.value = jackettService
                .queryResults
                .toMutableList()
        }
    }

    override fun onQueryStateChange(queryState: QueryState) {
        viewModelScope.launch {
            _liveDataQueryState.value = queryState
        }
    }

    fun setQuery(query: Query) = viewModelScope.launch {
        analyticService.logEvent(AnalyticEvent.Search)
        _liveDataIndexerQueryResults.postValue(mutableListOf())
        _liveDataSelectedIndexer.postValue(null)
        jackettService.query(query)
    }

    fun cancelQuery() = viewModelScope.launch {
        jackettService.cancelQuery()
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

    fun getUserSettings(): UserSettings = userSettingsRepository.read()

    private fun getIndexerQueryResults(): List<IndexerQueryResult> {
        val queryResults = _liveDataIndexerQueryResults.value ?: emptyList()

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
