package com.masterwok.shrimplesearch.common.extensions

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager


/**
 * Because hiding the keyboard on Android is a tedious process,
 * this is an extension which does that for us.
 */
internal fun Activity.hideSoftKeyboard() {
    (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).run {
        val view = currentFocus ?: View(this@hideSoftKeyboard)

        hideSoftInputFromWindow(view.windowToken, 0)
    }
}
