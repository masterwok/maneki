package com.masterwok.shrimplesearch.common.data.repositories

import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
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
    indexerIdBlockList: List<String>
) : JackettService {

    private val jackettHarnessListener: IJackettHarnessListener =
        JackettHarnessListener(this, indexerIdBlockList)

    private val listeners = mutableListOf<JackettService.Listener>()

    init {
        jackettHarness.setListener(jackettHarnessListener)
    }

    override val isInitialized: Boolean get() = jackettHarness.isInitialized
    override val queryState: QueryState? get() = jackettHarness.queryState

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

    override fun cancelQuery() = jackettHarness.cancelQuery()

    override suspend fun getIndexerCount(): Int = withContext(Dispatchers.IO) {
        jackettHarness.getIndexerCount()
    }

    override fun addListener(listener: JackettService.Listener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: JackettService.Listener) {
        listeners.remove(listener)
    }

    private class JackettHarnessListener(
        jackettService: JackettServiceImpl,
        private val indexerIdBlockList: List<String>
    ) : IJackettHarnessListener {

        private val weakJackettService = WeakReference(jackettService)

        override fun onIndexersInitialized() = weakJackettService.get().notNull { jackettService ->
            jackettService.listeners.forEach { it.onIndexersInitialized() }
        }

        override fun onIndexerInitialized() = weakJackettService.get().notNull { jackettService ->
            jackettService.listeners.forEach { it.onIndexerInitialized() }
        }

        override fun onIndexerQueryResult(
            indexerQueryResult: IndexerQueryResult
        ) = weakJackettService.get().notNull { jackettService ->
            // Don't notify subscribers of blocked indexer results.
            if (!indexerIdBlockList.contains(indexerQueryResult.indexer.id)) {
                jackettService.listeners.forEach { it.onIndexerQueryResult(indexerQueryResult) }
            }
        }

        override fun onQueryStateChange(queryState: QueryState) =
            weakJackettService.get().notNull { jackettService ->
                jackettService.listeners.forEach { it.onQueryStateChange(queryState) }
            }

    }

}