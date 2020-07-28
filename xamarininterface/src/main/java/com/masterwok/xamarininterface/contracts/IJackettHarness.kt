package com.masterwok.xamarininterface.contracts

interface IJackettHarness {

    val isInitialized: Boolean

    fun initialize()

    fun setListener(jackettHarnessListener: IJackettHarnessListener)

}