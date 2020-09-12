package com.masterwok.shrimplesearch.common.data.models

import com.masterwok.shrimplesearch.common.constants.Theme
import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val theme: Theme,
    val areScrollToTopNotificationsEnabled: Boolean
) {
    companion object
}

fun UserSettings.Companion.from(
    persistedUserSettings: PersistedUserSettings,
    defaultUserSettings: UserSettings
) = UserSettings(
    theme = persistedUserSettings.theme,
    areScrollToTopNotificationsEnabled = persistedUserSettings.areScrollToTopNotificationsEnabled
        ?: defaultUserSettings.areScrollToTopNotificationsEnabled
)