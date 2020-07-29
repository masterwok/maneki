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

    private val jackettHarnessListener: IJackettHarnessListener = JackettHarnessListener(this)

    private val listeners = mutableListOf<JackettService.Listener>()

    init {
        jackettHarness.setListener(jackettHarnessListener)
    }

    override val isInitialized: Boolean get() = jackettHarness.isInitialized

    @ExperimentalCoroutinesApi
    override suspend fun initialize() = withContext(Dispatchers.IO) {
        if (!isInitialized) {
            jackettHarness.initialize()
        }
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

    private class JackettHarnessListener(
        jackettService: JackettServiceImpl
    ) : IJackettHarnessListener {

        private val weakJackettService = WeakReference(jackettService)

        override fun onIndexersInitialized() = weakJackettService.get().notNull { jackettService ->
            jackettService.listeners.forEach { it.onIndexersInitialized() }
        }

        override fun onIndexerInitialized() = weakJackettService.get().notNull { jackettService ->
            jackettService.listeners.forEach { it.onIndexerInitialized() }
        }
    }

}