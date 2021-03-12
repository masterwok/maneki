package com.masterwok.shrimplesearch.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import com.masterwok.shrimplesearch.common.data.repositories.contracts.UserSettingsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    private val jackettService: JackettService
) : ViewModel() {

    val themeId get() = userSettingsRepository.getThemeId()

    val isExitDialogEnabled
        get() = userSettingsRepository
            .read()
            .isExitDialogEnabled

    fun disableExitDialog() = viewModelScope.launch {
        userSettingsRepository.update(
            userSettingsRepository
                .read()
                .copy(isExitDialogEnabled = false)
        )
    }

    fun cancelQuery() = viewModelScope.launch {
        jackettService.cancelQuery()
    }
}