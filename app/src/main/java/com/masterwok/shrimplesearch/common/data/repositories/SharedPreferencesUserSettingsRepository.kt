package com.masterwok.shrimplesearch.common.data.repositories

import android.content.Context
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.constants.Theme
import com.masterwok.shrimplesearch.common.data.models.PersistedUserSettings
import com.masterwok.shrimplesearch.common.data.models.UserSettings
import com.masterwok.shrimplesearch.common.data.models.from
import com.masterwok.shrimplesearch.common.data.repositories.contracts.UserSettingsRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Named

class SharedPreferencesUserSettingsRepository @Inject constructor(
    appContext: Context,
    @Named("shared_preferences_name") sharedPreferencesName: String,
    @Named("default_user_settings") private val defaultUserSettings: UserSettings
) : UserSettingsRepository {

    private val sharedPreferences = appContext.getSharedPreferences(
        sharedPreferencesName,
        Context.MODE_PRIVATE
    )

    override fun read(): UserSettings {
        val serialized = sharedPreferences
            .getString(NAME_USER_SETTINGS, null)
            ?: return defaultUserSettings

        return try {
            UserSettings.from(
                Json.decodeFromString(serialized),
                defaultUserSettings
            )
        } catch (ignored: Exception) {
            defaultUserSettings
        }
    }

    override fun update(userSettings: UserSettings) {
        sharedPreferences
            .edit()
            .putString(NAME_USER_SETTINGS, Json.encodeToString(userSettings))
            .apply()
    }

    override fun getThemeId(): Int = when (read().theme) {
        Theme.Light -> R.style.AppTheme
        Theme.Oled -> R.style.AppTheme_Oled
    }

    override fun getSplashThemeId() = when (read().theme) {
        Theme.Light -> R.style.AppTheme_Splash
        Theme.Oled -> R.style.AppTheme_Splash_Oled
    }

    companion object {
        private const val NAME_USER_SETTINGS = "preference.user_settings"
    }

}