package com.masterwok.xamarininterface.contracts

import com.masterwok.xamarininterface.models.Query

interface IJackettHarness {

    val isInitialized: Boolean

    fun initialize()

    fun setListener(jackettHarnessListener: IJackettHarnessListener)

    fun getIndexerCount(): Int

    fun query(query: Query)

    fun cancelQuery()

}