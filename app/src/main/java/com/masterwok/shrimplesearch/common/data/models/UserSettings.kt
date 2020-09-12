package com.masterwok.shrimplesearch.common.data.models

import com.masterwok.shrimplesearch.common.constants.Theme
import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val theme: Theme,
    val isScrollToTopNotificationsEnabled: Boolean,
    val isExistDialogEnabled: Boolean
) {
    companion object
}

fun UserSettings.Companion.from(
    persistedUserSettings: PersistedUserSettings,
    defaultUserSettings: UserSettings
) = UserSettings(
    theme = persistedUserSettings.theme,
    isScrollToTopNotificationsEnabled = persistedUserSettings.isScrollToTopNotificationsEnabled
        ?: defaultUserSettings.isScrollToTopNotificationsEnabled,
    isExistDialogEnabled = persistedUserSettings.isExistDialogEnabled
        ?: defaultUserSettings.isExistDialogEnabled
)