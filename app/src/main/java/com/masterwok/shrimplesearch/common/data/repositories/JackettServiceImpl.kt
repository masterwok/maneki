package com.masterwok.shrimplesearch.common.data.repositories

import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import com.masterwok.shrimplesearch.common.utils.notNull
import com.masterwok.xamarininterface.contracts.IJackettHarness
import com.masterwok.xamarininterface.contracts.IJackettHarnessListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

class JackettServiceImpl constructor(
    private val jackettHarness: IJackettHarness
) : JackettService {

    private val jackettHarnessListener: JackettHarnessListener = JackettHarnessListener(this)

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

    private class JackettHarnessListener(
        jackettService: JackettService
    ) : IJackettHarnessListener {

        private val weakJackettService = WeakReference(jackettService)

        override fun onIndexersInitialized() = weakJackettService.get().notNull { jackettService ->

        }

        override fun OnIndexerInitialized() = weakJackettService.get().notNull { jackettService ->

        }

    }

}