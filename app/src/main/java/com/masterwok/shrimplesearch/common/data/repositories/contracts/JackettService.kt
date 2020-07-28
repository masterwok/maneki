package com.masterwok.shrimplesearch.common.data.repositories.contracts


interface JackettService {

    val isInitialized: Boolean

    suspend fun initialize()

}