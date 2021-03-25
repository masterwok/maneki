package com.masterwok.shrimplesearch.common.data.repositories.contracts

import com.masterwok.shrimplesearch.common.data.models.UserSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {

    fun read(): UserSettings

    suspend fun update(userSettings: UserSettings)

    fun getThemeId(): Int

    fun getSplashThemeId(): Int

    @ExperimentalCoroutinesApi
    fun getUserSettingsAsFlow(): Flow<UserSettings>

}