package com.masterwok.shrimplesearch.common.extensions

import kotlin.math.ln
import kotlin.math.pow

/**
 * Convert the [Long] to a human readable unit string. Similar to doing `ls -h` in bash.
 */
fun Long.toHumanReadableByteCount(
    si: Boolean = false
): String {
    val unit = if (si) 1000 else 1024

    if (this < unit) return toString() + " B"

    val exp = (ln(toDouble()) / ln(unit.toDouble())).toInt()

    val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"

    return String.format(
        "%.1f %sB"
        , this / unit.toDouble().pow(exp.toDouble())
        , pre
    )
}