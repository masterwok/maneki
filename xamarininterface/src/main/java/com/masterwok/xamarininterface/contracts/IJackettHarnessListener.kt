package com.masterwok.xamarininterface.contracts

import com.masterwok.xamarininterface.enums.QueryState

interface IJackettHarnessListener {

    fun onIndexersInitialized()

    fun onIndexerInitialized()

    fun onResultsUpdated()

    fun onQueryStateChange(queryState: QueryState)

}