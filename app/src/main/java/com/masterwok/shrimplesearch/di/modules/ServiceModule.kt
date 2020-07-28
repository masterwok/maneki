package com.masterwok.shrimplesearch.di.modules

import com.masterwok.shrimplesearch.common.data.repositories.JackettServiceImpl
import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import com.masterwok.xamarin.JackettHarnessFactory
import com.masterwok.xamarininterface.contracts.ICardigannDefinitionRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        JackettHarnessModule::class
    ]
)
class ServiceModule {

    @Singleton
    @Provides
    fun provideJackettService(
        jackettHarnessFactory: JackettHarnessFactory
        , cardigannDefinitionRepository: ICardigannDefinitionRepository
    ): JackettService = JackettServiceImpl(
        jackettHarnessFactory.createInstance(
            cardigannDefinitionRepository
        )
    )

}