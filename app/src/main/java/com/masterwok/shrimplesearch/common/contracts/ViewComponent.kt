package com.masterwok.shrimplesearch.common.contracts

/**
 * This contract provides a pattern to follow when configuring and reading view state from custom
 * view components.
 */
internal interface ViewComponent<T> : Configurable<T> {

    /**
     * Get the current view component state.
     */
    fun getModel(): T
}
