package com.masterwok.shrimplesearch.common.data.repositories

import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import com.masterwok.shrimplesearch.common.data.repositories.contracts.UserSettingsRepository
import com.masterwok.shrimplesearch.common.utils.notNull
import com.masterwok.xamarininterface.enums.QueryState
import com.masterwok.xamarininterface.contracts.IJackettHarness
import com.masterwok.xamarininterface.contracts.IJackettHarnessListener
import com.masterwok.xamarininterface.models.IndexerQueryResult
import com.masterwok.xamarininterface.models.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

class JackettServiceImpl constructor(
    private val jackettHarness: IJackettHarness,
    private val userSettingsRepository: UserSettingsRepository,
    private val indexerBlockList: List<String>
) : JackettService {

    private val jackettHarnessListener: IJackettHarnessListener = JackettHarnessListener(this)

    private val listeners = mutableListOf<JackettService.Listener>()

    private val userSettings get() = userSettingsRepository.read()

    init {
        jackettHarness.setListener(jackettHarnessListener)
    }

    override val isInitialized: Boolean get() = jackettHarness.isInitialized

    override val queryState: QueryState? get() = jackettHarness.queryState

    override val queryResults: List<IndexerQueryResult>
        get() = jackettHarness
            .queryResults
            .filterNot { indexerBlockList.contains(it.indexer.id) }
            .map { indexerQueryResult ->
                if (!userSettings.isOnlyMagnetQueryResultItemsEnabled) {
                    indexerQueryResult
                } else {
                    indexerQueryResult.copy(
                        items = indexerQueryResult
                            .items
                            .filterNot { it.linkInfo.magnetUri == null },
                        linkCount = 0
                    )
                }
            }

    @ExperimentalCoroutinesApi
    override suspend fun initialize() = withContext(Dispatchers.IO) {
        if (!isInitialized) {
            jackettHarness.initialize()
        }
    }

    override suspend fun query(query: Query) = withContext(Dispatchers.IO) {
        jackettHarness.cancelQuery()
        jackettHarness.query(query)
    }

    override suspend fun cancelQuery() = withContext(Dispatchers.IO) {
        jackettHarness.cancelQuery()
    }

    override suspend fun getIndexerCount(): Int = withContext(Dispatchers.IO) {
        jackettHarness.getIndexerCount()
    }

    override fun addListener(listener: JackettService.Listener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: JackettService.Listener) {
        listeners.remove(listener)
    }

    private class JackettHarnessListener(jackettService: JackettServiceImpl) :
        IJackettHarnessListener {

        private val weakJackettService = WeakReference(jackettService)

        override fun onIndexersInitialized() = weakJackettService.get().notNull { jackettService ->
            jackettService.listeners.forEach { it.onIndexersInitialized() }
        }

        override fun onIndexerInitialized() = weakJackettService.get().notNull { jackettService ->
            jackettService.listeners.forEach { it.onIndexerInitialized() }
        }

        override fun onResultsUpdated() = weakJackettService.get().notNull { jackettService ->
            if (jackettService.queryState != QueryState.Aborted) {
                jackettService.listeners.forEach { it.onResultsUpdated() }
            }
        }

        override fun onQueryStateChange(queryState: QueryState) =
            weakJackettService.get().notNull { jackettService ->
                jackettService.listeners.forEach { it.onQueryStateChange(queryState) }
            }
    }

}