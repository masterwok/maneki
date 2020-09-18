package com.masterwok.shrimplesearch.common.data.services.contracts

import com.masterwok.shrimplesearch.common.constants.AnalyticEvent
import java.lang.Exception

interface AnalyticService {
    fun logEvent(event: AnalyticEvent)
    fun logException(exception: Exception, message: String? = null)
    fun logScreen(screenClass: Class<*>)
}