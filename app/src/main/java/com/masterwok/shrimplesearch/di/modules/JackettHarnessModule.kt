package com.masterwok.shrimplesearch.di.modules

import com.masterwok.shrimplesearch.common.data.repositories.CardigannDefinitionRepository
import com.masterwok.xamarin.JackettHarnessFactory
import com.masterwok.xamarin.JackettHarnessFactoryImpl
import com.masterwok.xamarininterface.contracts.ICardigannDefinitionRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [JackettHarnessModule.Declarations::class])
class JackettHarnessModule {

    @Suppress("RedundantModalityModifier", "unused")
    @Module
    interface Declarations {
        @Singleton
        @Binds
        abstract fun bindCardigannDefinitionRepository(
            cardigannDefinitionRepository: CardigannDefinitionRepository
        ): ICardigannDefinitionRepository
    }

    @Singleton
    @Provides
    fun provideJackettFactory(): JackettHarnessFactory = JackettHarnessFactoryImpl

}