package com.masterwok.shrimplesearch.features.settings.di

import com.masterwok.shrimplesearch.features.settings.fragments.SettingsFragment
import dagger.Subcomponent

@SettingsScope
@Subcomponent(
    modules = [
        SettingsModule::class
    ]
)interface SettingsSubcomponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SettingsSubcomponent
    }

    fun inject(fragment: SettingsFragment)

}