package com.masterwok.shrimplesearch.common.data.repositories

import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import com.masterwok.xamarininterface.contracts.IJackettHarness
import com.masterwok.xamarininterface.contracts.IJackettHarnessListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext

class JackettServiceImpl constructor(
    private val jackettHarness: IJackettHarness
) : JackettService {

    private val jackettHarnessListener = object : IJackettHarnessListener {
        override fun onIndexersInitialized() {
            val indexerCount = jackettHarness.getIndexerCount()
        }

        override fun OnIndexerInitialized() {
        }
    }

    init {
        jackettHarness.setListener(jackettHarnessListener)
    }

    override val isInitialized: Boolean get() = jackettHarness.isInitialized

    @ExperimentalCoroutinesApi
    override suspend fun initialize() = withContext(Dispatchers.Default) {
        if (!isInitialized) {
            jackettHarness.initialize()
        }
    }

}