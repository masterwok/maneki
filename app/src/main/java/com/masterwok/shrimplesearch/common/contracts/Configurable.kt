package com.masterwok.shrimplesearch.common.contracts

interface Configurable<T> {

    fun configure(model: T)

}