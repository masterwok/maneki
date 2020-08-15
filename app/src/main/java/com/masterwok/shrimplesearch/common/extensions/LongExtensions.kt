package com.masterwok.shrimplesearch.common.extensions

import java.text.DecimalFormat
import kotlin.math.ln
import kotlin.math.pow

private val decimalFormat = DecimalFormat("#,###.00")

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
    val value = this / unit.toDouble().pow(exp.toDouble())

    return String.format(
        "%s %sB"
        , decimalFormat.format(value)
        , pre
    )
}