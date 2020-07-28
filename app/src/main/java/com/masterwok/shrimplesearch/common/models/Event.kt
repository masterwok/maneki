package com.masterwok.shrimplesearch.common.models

/**
 * This class represents some emitted event.
 */
class Event<out T>(private val data: T) {

    /**
     * Whether or not the event has been consumed.
     */
    var isConsumed = false
        private set

    /**
     * Consume the [Event]. An event can only be consumed once; subsequent calls will return null.
     * Use [peek] to see the consumed [data] of the [Event].
     */
    fun consume(): T? {
        if (isConsumed) {
            return null
        }

        isConsumed = true

        return data
    }

    /**
     * Get the [data] of the [Event] regardless of whether or not the [Event] has already been consumed.
     */
    fun peek() = data
}