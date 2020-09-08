package com.masterwok.shrimplesearch.common.data.repositories

import android.content.Context
import com.masterwok.shrimplesearch.common.constants.Theme
import com.masterwok.shrimplesearch.common.data.models.UserSettings
import com.masterwok.shrimplesearch.common.data.repositories.contracts.UserSettingsRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Named

class SharedPreferencesUserSettingsRepository @Inject constructor(
    appContext: Context,
    @Named("shared_preferences_name") sharedPreferencesName: String
) : UserSettingsRepository {

    private val sharedPreferences = appContext.getSharedPreferences(
        sharedPreferencesName,
        Context.MODE_PRIVATE
    )

    override fun read(): UserSettings {
        val serialized = sharedPreferences
            .getString(NAME_USER_SETTINGS, null)
            ?: return defaultUserSettings

        return Json.decodeFromString(serialized)
    }

    override fun update(userSettings: UserSettings) {
        sharedPreferences
            .edit()
            .putString(NAME_USER_SETTINGS, Json.encodeToString(userSettings))
            .apply()
    }

    companion object {
        private const val NAME_USER_SETTINGS = "preference.user_settings"

        private val defaultUserSettings = UserSettings(
            theme = Theme.Oled
        )
    }

}