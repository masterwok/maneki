package com.masterwok.xamarin

import com.masterwok.jackett.JackettHarness
import com.masterwok.xamarininterface.contracts.ICardigannDefinitionRepository
import com.masterwok.xamarininterface.contracts.IJackettHarness

object JackettHarnessFactory {
    fun createInstance(
        cardigannDefinitionRepository: ICardigannDefinitionRepository
    ): IJackettHarness = JackettHarness(
        cardigannDefinitionRepository
    )
}