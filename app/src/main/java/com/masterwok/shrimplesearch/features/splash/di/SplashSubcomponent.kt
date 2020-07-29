package com.masterwok.shrimplesearch.features.splash.di

import com.masterwok.shrimplesearch.di.modules.ViewModelFactoryModule
import com.masterwok.shrimplesearch.features.splash.activities.SplashActivity
import dagger.Subcomponent

@SplashScope
@Subcomponent(
    modules = [
        ViewModelFactoryModule::class,
        SplashModule::class
    ]
)
interface SplashSubcomponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): SplashSubcomponent
    }

    fun inject(activity: SplashActivity)

}