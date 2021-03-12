package com.masterwok.shrimplesearch.common.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.constants.Theme
import com.masterwok.shrimplesearch.common.data.models.UserSettings
import com.masterwok.shrimplesearch.common.data.models.from
import com.masterwok.shrimplesearch.common.data.repositories.contracts.UserSettingsRepository
import com.masterwok.shrimplesearch.di.modules.RepositoryModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Named
import kotlin.system.measureTimeMillis

class SharedPreferencesUserSettingsRepository @Inject constructor(
    appContext: Context,
    @Named(RepositoryModule.NAMED_SHARED_PREFERENCES_NAME) sharedPreferencesName: String,
    @Named(RepositoryModule.NAMED_DEFAULT_USER_SETTINGS) private val defaultUserSettings: UserSettings
) : UserSettingsRepository {

    private val sharedPreferences = appContext.getSharedPreferences(
        sharedPreferencesName,
        Context.MODE_PRIVATE
    )

    @ExperimentalCoroutinesApi
    override fun getUserSettingsAsFlow() = callbackFlow {
        send(read())

        val callback = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == NAME_USER_SETTINGS) {
                sendBlocking(read())
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(callback)

        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(callback) }
    }

    override fun read(): UserSettings {
        val serialized = sharedPreferences
            .getString(NAME_USER_SETTINGS, null)
            ?: return defaultUserSettings

        return try {
            UserSettings.from(
                Json { ignoreUnknownKeys = true }.decodeFromString(serialized),
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