package com.masterwok.shrimplesearch.common.data.models

import com.masterwok.shrimplesearch.common.constants.Theme
import kotlinx.serialization.Serializable

@Serializable
data class PersistedUserSettings(
    val theme: Theme,
    val areScrollToTopNotificationsEnabled: Boolean? = false
)
