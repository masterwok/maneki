package com.masterwok.shrimplesearch.common.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

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