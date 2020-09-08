package com.masterwok.shrimplesearch.common.data.repositories.contracts

import com.masterwok.shrimplesearch.common.data.models.UserSettings

interface UserSettingsRepository {

    fun read(): UserSettings

    fun update(userSettings: UserSettings)

    fun getThemeId(): Int

}