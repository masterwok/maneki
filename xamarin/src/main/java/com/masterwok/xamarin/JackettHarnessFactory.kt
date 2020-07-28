package com.masterwok.xamarin

import com.masterwok.xamarininterface.contracts.ICardigannDefinitionRepository
import com.masterwok.xamarininterface.contracts.IJackettHarness
import com.masterwok.xamarininterface.contracts.IJackettHarnessListener

interface JackettHarnessFactory {
    fun createInstance(
        cardigannDefinitionRepository: ICardigannDefinitionRepository
    ) : IJackettHarness
}