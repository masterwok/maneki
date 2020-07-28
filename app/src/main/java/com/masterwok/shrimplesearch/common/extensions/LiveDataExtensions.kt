package com.masterwok.shrimplesearch.common.extensions

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.masterwok.shrimplesearch.common.models.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import java.lang.Exception

/**
 * Track an operation through [Event] states.
 */
internal suspend fun <T> MutableLiveData<Event<EventState>>.trackEvent(
    block: suspend () -> T
): T? = coroutineScope {
    postValue(Event(EventPending))

    var networkFailure: Event<EventFailure>? = null

    val deferred: Deferred<T?> = async {
        try {
            block()
        } catch (ex: Exception) {
            networkFailure = Event(EventFailure(ex.message ?: "Error"))
            Log.e("TRACK_REQUEST", "Async call failed", ex)
            null
        }
    }

    delay(1000)

    deferred.await().also {
        postValue(networkFailure ?: Event(EventSuccess))
    }
}