package com.masterwok.shrimplesearch.common.data.repositories.contracts

import com.masterwok.xamarininterface.enums.QueryState
import com.masterwok.xamarininterface.models.IndexerQueryResult
import com.masterwok.xamarininterface.models.Query


interface JackettService {

    val isInitialized: Boolean

    val queryState: QueryState?

    suspend fun initialize()

    suspend fun query(query: Query)

    fun cancelQuery()

    suspend fun getIndexerCount(): Int

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    interface Listener {

        fun onIndexersInitialized()

        fun onIndexerInitialized()

        fun onIndexerQueryResult(indexerQueryResult: IndexerQueryResult)

        fun onQueryStateChange(queryState: QueryState)

    }

}