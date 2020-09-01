package com.masterwok.shrimplesearch.common.data.services.contracts

import com.masterwok.shrimplesearch.common.constants.AnalyticEvent

interface AnalyticService {
    fun logEvent(event: AnalyticEvent)
}