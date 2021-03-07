package com.masterwok.shrimplesearch.di.modules

import com.masterwok.shrimplesearch.common.INDEXER_BLOCK_LIST
import com.masterwok.shrimplesearch.common.data.repositories.JackettServiceImpl
import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import com.masterwok.shrimplesearch.common.data.repositories.contracts.UserSettingsRepository
import com.masterwok.shrimplesearch.common.data.services.FirebaseAnalyticsService
import com.masterwok.shrimplesearch.common.data.services.contracts.AnalyticService
import com.masterwok.xamarin.JackettHarnessFactory
import com.masterwok.xamarininterface.contracts.ICardigannDefinitionRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        JackettHarnessModule::class,
        ServiceModule.Declarations::class
    ]
)
class ServiceModule {

    @Suppress("RedundantModalityModifier", "unused")
    @Module
    interface Declarations {
        @Singleton
        @Binds
        abstract fun bindAnalyticService(
            firebaseAnalyticsService: FirebaseAnalyticsService
        ): AnalyticService
    }

    @Suppress("unused")
    @Singleton
    @Provides
    fun provideJackettService(
        jackettHarnessFactory: JackettHarnessFactory,
        userSettingsRepository: UserSettingsRepository,
        cardigannDefinitionRepository: ICardigannDefinitionRepository
    ): JackettService = JackettServiceImpl(
        jackettHarnessFactory.createInstance(cardigannDefinitionRepository),
        userSettingsRepository,
        INDEXER_BLOCK_LIST
    )

}