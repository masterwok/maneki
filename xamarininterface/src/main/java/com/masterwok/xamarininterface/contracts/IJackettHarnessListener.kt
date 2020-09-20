package com.masterwok.xamarininterface.contracts

import com.masterwok.xamarininterface.enums.QueryState
import com.masterwok.xamarininterface.models.IndexerQueryResult

interface IJackettHarnessListener {

    fun onIndexersInitialized()

    fun onIndexerInitialized()

    fun onIndexerQueryResult(indexerQueryResult: IndexerQueryResult)

    fun onQueryStateChange(queryState: QueryState)

}