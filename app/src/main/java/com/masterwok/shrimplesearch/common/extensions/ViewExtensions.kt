package com.masterwok.shrimplesearch.common.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

internal fun View.showSnackbar(
    message: CharSequence,
    length: Int,
    actionMessage: CharSequence? = null,
    backgroundColor: Int,
    textColor: Int,
    action: ((View) -> Unit)? = null
) = Snackbar.make(this, message, length).apply {
    view.setBackgroundColor(backgroundColor)

    setTextColor(textColor)

    if (actionMessage != null && action != null) {
        setAction(actionMessage, action)
        setActionTextColor(textColor)
    }

    show()
}

@ExperimentalCoroutinesApi
fun View.onClicked() = callbackFlow {
    setOnClickListener { offer(Unit) }
    awaitClose { setOnClickListener(null) }
}
