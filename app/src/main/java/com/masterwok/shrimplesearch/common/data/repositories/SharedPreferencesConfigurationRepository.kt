package com.masterwok.shrimplesearch.common.data.repositories

import android.content.Context
import com.masterwok.shrimplesearch.common.data.repositories.contracts.ConfigurationRepository
import javax.inject.Inject
import javax.inject.Named

class SharedPreferencesConfigurationRepository @Inject constructor(
    appContext: Context,
    @Named("shared_preferences_name") sharedPreferencesName: String
) : ConfigurationRepository {

    private val sharedPreferences = appContext.getSharedPreferences(
        sharedPreferencesName,
        Context.MODE_PRIVATE
    )

    override suspend fun incrementResultTapCount() {
        val resultItemCount = getResultItemTapCount()

        sharedPreferences
            .edit()
            .putInt(NAME_RESULT_ITEM_TAP_COUNT, resultItemCount + 1)
            .apply()
    }

    override suspend fun getResultItemTapCount(): Int = sharedPreferences.getInt(
        NAME_RESULT_ITEM_TAP_COUNT,
        0
    )

    companion object {
        private const val NAME_RESULT_ITEM_TAP_COUNT = "configuration.result_item_tap_count"
    }

}