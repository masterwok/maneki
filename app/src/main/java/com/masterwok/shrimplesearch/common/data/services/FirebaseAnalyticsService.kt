package com.masterwok.shrimplesearch.common.data.services

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.masterwok.shrimplesearch.common.constants.AnalyticEvent
import com.masterwok.shrimplesearch.common.data.services.contracts.AnalyticService
import java.lang.Exception
import javax.inject.Inject

class FirebaseAnalyticsService @Inject constructor() : AnalyticService {

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        Firebase.analytics
    }

    private val crashlytics: FirebaseCrashlytics by lazy {
        FirebaseCrashlytics.getInstance()
    }

    override fun logEvent(event: AnalyticEvent) = firebaseAnalytics.logEvent(event.eventName, null)

    override fun logException(exception: Exception, message: String?) {
        message?.let(crashlytics::log)
        crashlytics.recordException(exception)
    }
}