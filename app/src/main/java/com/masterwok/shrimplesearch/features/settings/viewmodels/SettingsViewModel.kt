package com.masterwok.shrimplesearch.features.settings.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.masterwok.shrimplesearch.common.data.models.UserSettings
import com.masterwok.shrimplesearch.common.data.repositories.contracts.UserSettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SettingsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    val liveDataUserSettings = userSettingsRepository
        .getUserSettingsAsFlow()
        .asLiveData(viewModelScope.coroutineContext)

    fun updateUserSettings(userSettings: UserSettings) = viewModelScope.launch {
        userSettingsRepository.update(userSettings)
    }

}