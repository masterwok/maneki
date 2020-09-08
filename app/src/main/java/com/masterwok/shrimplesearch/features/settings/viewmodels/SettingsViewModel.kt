package com.masterwok.shrimplesearch.features.settings.viewmodels

import androidx.lifecycle.ViewModel
import com.masterwok.shrimplesearch.common.data.models.UserSettings
import com.masterwok.shrimplesearch.common.data.repositories.contracts.UserSettingsRepository
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    fun readUserSettings(): UserSettings = userSettingsRepository.read()

    fun updateUserSettings(userSettings: UserSettings) = userSettingsRepository.update(userSettings)

}