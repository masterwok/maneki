package com.masterwok.xamarin

import com.masterwok.jackett.JackettHarness
import com.masterwok.xamarininterface.contracts.ICardigannDefinitionRepository
import com.masterwok.xamarininterface.contracts.IJackettHarness
import com.masterwok.xamarininterface.contracts.IJackettHarnessCallbacks

object JackettHarnessFactoryImpl : JackettHarnessFactory {

    override fun createInstance(
        cardigannDefinitionRepository: ICardigannDefinitionRepository
        , jackettHarnessCallbacks: IJackettHarnessCallbacks
    ): IJackettHarness = JackettHarness(
        cardigannDefinitionRepository
        , jackettHarnessCallbacks
    )

}