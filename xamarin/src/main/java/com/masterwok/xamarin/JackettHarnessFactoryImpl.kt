package com.masterwok.xamarin

import com.masterwok.jackett.JackettHarness
import com.masterwok.xamarininterface.contracts.ICardigannDefinitionRepository
import com.masterwok.xamarininterface.contracts.IJackettHarness
import com.masterwok.xamarininterface.contracts.IJackettHarnessListener

object JackettHarnessFactoryImpl : JackettHarnessFactory {

    override fun createInstance(
        cardigannDefinitionRepository: ICardigannDefinitionRepository
    ): IJackettHarness = JackettHarness(
        cardigannDefinitionRepository
    )

}