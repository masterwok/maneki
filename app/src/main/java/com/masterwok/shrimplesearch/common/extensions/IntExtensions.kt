package com.masterwok.shrimplesearch.common.extensions

import android.content.Context


/**
 * Convert value from device pixel unit to pixel unit.
 *
 * @param context the current context.
 * @return the equivalent pixel unit.
 */
fun Int.dpToPx(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()
