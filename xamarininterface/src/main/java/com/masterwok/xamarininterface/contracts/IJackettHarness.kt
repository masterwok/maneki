package com.masterwok.xamarininterface.contracts

import com.masterwok.xamarininterface.enums.QueryState
import com.masterwok.xamarininterface.models.IndexerQueryResult
import com.masterwok.xamarininterface.models.Query

interface IJackettHarness {

    val isInitialized: Boolean

    val queryState: QueryState?

    val queryResults: List<IndexerQueryResult>

    fun initialize()

    fun setListener(jackettHarnessListener: IJackettHarnessListener)

    fun getIndexerCount(): Int

    fun query(query: Query)

    fun cancelQuery()

}