package com.masterwok.shrimplesearch.di.modules

import com.masterwok.shrimplesearch.common.SHARED_PREFERENCES_NAME
import com.masterwok.shrimplesearch.common.data.repositories.SharedPreferencesUserSettingsRepository
import com.masterwok.shrimplesearch.common.data.repositories.contracts.UserSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module(
    includes = [
        RepositoryModule.Declarations::class
    ]
)
class RepositoryModule {

    @Suppress("RedundantModalityModifier", "unused")
    @Module
    interface Declarations {
        @Singleton
        @Binds
        abstract fun bindSharedPreferencesUserSettingsRepository(
            sharedPreferencesUserSettingsRepository: SharedPreferencesUserSettingsRepository
        ): UserSettingsRepository
    }

    @Provides
    @Named("shared_preferences_name")
    fun provideSharedPreferencesName(): String = SHARED_PREFERENCES_NAME

}