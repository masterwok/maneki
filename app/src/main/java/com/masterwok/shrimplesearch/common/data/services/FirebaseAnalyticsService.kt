package com.masterwok.shrimplesearch.common.data.services

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.masterwok.shrimplesearch.common.constants.AnalyticEvent
import com.masterwok.shrimplesearch.common.data.services.contracts.AnalyticService
import javax.inject.Inject

class FirebaseAnalyticsService @Inject constructor() : AnalyticService {

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        Firebase.analytics
    }

    override fun logEvent(event: AnalyticEvent) = firebaseAnalytics.logEvent(event.eventName, null)

}