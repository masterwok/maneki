package com.masterwok.shrimplesearch.common.data.repositories

import android.content.Context
import com.masterwok.shrimplesearch.common.data.repositories.contracts.ConfigurationRepository
import com.masterwok.shrimplesearch.di.modules.RepositoryModule
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class SharedPreferencesConfigurationRepository @Inject constructor(
    appContext: Context,
    @Named(RepositoryModule.NAMED_SHARED_PREFERENCES_NAME) sharedPreferencesName: String
) : ConfigurationRepository {

    private val sharedPreferences = appContext.getSharedPreferences(
        sharedPreferencesName,
        Context.MODE_PRIVATE
    )

    override suspend fun incrementResultTapCount() {
        val resultItemCount = getResultItemTapCount()

        sharedPreferences
            .edit()
            .putLong(NAME_RESULT_ITEM_TAP_COUNT, resultItemCount + 1L)
            .apply()
    }

    override suspend fun getResultItemTapCount(): Long = sharedPreferences.getLong(
        NAME_RESULT_ITEM_TAP_COUNT,
        0
    )

    companion object {
        private const val NAME_RESULT_ITEM_TAP_COUNT = "configuration.result_item_tap_count"
    }

}