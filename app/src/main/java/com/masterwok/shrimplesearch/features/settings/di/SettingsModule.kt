package com.masterwok.shrimplesearch.features.settings.di

import androidx.lifecycle.ViewModel
import com.masterwok.shrimplesearch.common.AGGREGATE_INDEXER_ID
import com.masterwok.shrimplesearch.common.SHARED_PREFERENCES_NAME
import com.masterwok.shrimplesearch.di.annotations.ViewModelKey
import com.masterwok.shrimplesearch.features.settings.viewmodels.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named


@Module(includes = [SettingsModule.Declarations::class])
class SettingsModule {

    @Suppress("RedundantModalityModifier", "unused")
    @Module
    interface Declarations {
        @SettingsScope
        @Binds
        @IntoMap
        @ViewModelKey(SettingsViewModel::class)
        abstract fun bindViewModel(viewModel: SettingsViewModel): ViewModel
    }

}