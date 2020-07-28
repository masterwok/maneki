package com.masterwok.shrimplesearch.common.data.repositories

import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import com.masterwok.xamarininterface.contracts.IJackettHarness
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext

class JackettServiceImpl constructor(
    private val jackettHarness: IJackettHarness
) : JackettService {

    override val isInitialized: Boolean get() = jackettHarness.isInitialized

    @ExperimentalCoroutinesApi
    override suspend fun initialize() = withContext(Dispatchers.Default) {
        jackettHarness.initialize()
    }

}