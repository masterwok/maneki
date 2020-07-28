package com.masterwok.shrimplesearch.di.modules

import com.masterwok.shrimplesearch.common.data.callbacks.JackettHarnessCallbacks
import com.masterwok.shrimplesearch.common.data.repositories.CardigannDefinitionRepository
import com.masterwok.shrimplesearch.common.data.repositories.JackettServiceImpl
import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import com.masterwok.xamarin.JackettHarnessFactory
import com.masterwok.xamarin.JackettHarnessFactoryImpl
import com.masterwok.xamarininterface.contracts.ICardigannDefinitionRepository
import com.masterwok.xamarininterface.contracts.IJackettHarnessCallbacks
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        ServiceModule.Declarations::class
    ]
)
class ServiceModule {

    @Module
    interface Declarations {
//        @Singleton
//        @Binds
//        abstract fun bindJackettFactory(jackettServiceImpl: JackettHarnessFactoryImpl): JackettHarnessFactory

        @Singleton
        @Binds
        abstract fun bindCardigannDefinitionRepository(
            cardigannDefinitionRepository: CardigannDefinitionRepository
        ): ICardigannDefinitionRepository

        @Singleton
        @Binds
        abstract fun bindJackettHarnessCallbacks(
            jackettHarnessCallbacks: JackettHarnessCallbacks
        ): IJackettHarnessCallbacks
    }

    @Singleton
    @Provides
    fun provideJackettFactory() : JackettHarnessFactory = JackettHarnessFactoryImpl

    @Singleton
    @Provides
    fun provideJackettService(
        jackettHarnessFactory: JackettHarnessFactory
        , cardigannDefinitionRepository: ICardigannDefinitionRepository
        , jackettHarnessCallbacks: IJackettHarnessCallbacks
    ): JackettService = JackettServiceImpl(
        jackettHarnessFactory.createInstance(
            cardigannDefinitionRepository
            , jackettHarnessCallbacks
        )
    )

}