package com.masterwok.shrimplesearch.di.modules

import com.masterwok.shrimplesearch.common.DEFAULT_USER_SETTINGS
import com.masterwok.shrimplesearch.common.SHARED_PREFERENCES_NAME
import com.masterwok.shrimplesearch.common.data.models.UserSettings
import com.masterwok.shrimplesearch.common.data.repositories.SharedPreferencesConfigurationRepository
import com.masterwok.shrimplesearch.common.data.repositories.SharedPreferencesUserSettingsRepository
import com.masterwok.shrimplesearch.common.data.repositories.contracts.ConfigurationRepository
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

        @Singleton
        @Binds
        abstract fun bindConfigurationRepository(
            sharedPreferencesUserConfigurationRepository: SharedPreferencesConfigurationRepository
        ): ConfigurationRepository
    }

    @Provides
    @Named(NAMED_SHARED_PREFERENCES_NAME)
    fun provideSharedPreferencesName(): String = SHARED_PREFERENCES_NAME

    @Provides
    @Named(NAMED_DEFAULT_USER_SETTINGS)
    fun provideDefaultUserSettings(): UserSettings = DEFAULT_USER_SETTINGS

    companion object {
        const val NAMED_SHARED_PREFERENCES_NAME = "shared_preferences_name"
        const val NAMED_DEFAULT_USER_SETTINGS = "default_user_settings"
    }
}