package com.masterwok.shrimplesearch.common.data.repositories.contracts

interface ConfigurationRepository {
    suspend fun incrementResultTapCount()
    suspend fun getResultItemTapCount(): Long
}