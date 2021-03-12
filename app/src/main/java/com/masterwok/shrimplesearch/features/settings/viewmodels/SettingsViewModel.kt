package com.masterwok.shrimplesearch.features.settings.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.masterwok.shrimplesearch.common.data.models.UserSettings
import com.masterwok.shrimplesearch.common.data.repositories.contracts.UserSettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    val liveDataUserSettings = userSettingsRepository
        .getUserSettingsAsFlow()
        .asLiveData(viewModelScope.coroutineContext)

    fun readUserSettings(): UserSettings = userSettingsRepository.read()

    fun updateUserSettings(userSettings: UserSettings) = userSettingsRepository.update(userSettings)

}