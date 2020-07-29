package com.masterwok.shrimplesearch.common.utils

/**
 * Invoke the provided [block] if and only if it's not null.
 */
internal inline fun <T> T?.notNull(block: (T) -> Unit) {
    this?.let(block)
}