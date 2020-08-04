package com.masterwok.xamarininterface.contracts

import com.masterwok.xamarininterface.models.IndexerQueryResult

interface IJackettHarnessListener {

    fun onIndexersInitialized()

    fun onIndexerInitialized()

    fun onIndexerQueryResult(indexerQueryResult: IndexerQueryResult)

}