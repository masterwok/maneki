package com.masterwok.xamarin

import com.masterwok.xamarininterface.contracts.ICardigannDefinitionRepository
import com.masterwok.xamarininterface.contracts.IJackettHarness
import com.masterwok.xamarininterface.contracts.IJackettHarnessCallbacks

interface JackettHarnessFactory {
    fun createInstance(
        cardigannDefinitionRepository: ICardigannDefinitionRepository
        , jackettHarnessCallbacks: IJackettHarnessCallbacks
    ) : IJackettHarness
}