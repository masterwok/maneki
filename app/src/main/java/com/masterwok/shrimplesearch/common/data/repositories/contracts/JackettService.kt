package com.masterwok.shrimplesearch.common.data.repositories.contracts


interface JackettService {

    val isInitialized: Boolean

    suspend fun initialize()

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    interface Listener {

        fun onIndexersInitialized()

        fun OnIndexerInitialized()
    }

}