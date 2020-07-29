package com.masterwok.shrimplesearch.common.data.repositories.contracts


interface JackettService {

    val isInitialized: Boolean

    suspend fun initialize()

    suspend fun getIndexerCount(): Int

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    interface Listener {

        fun onIndexersInitialized()

        fun onIndexerInitialized()
    }

}