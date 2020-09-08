package com.masterwok.shrimplesearch.di.components

import android.content.Context
import com.masterwok.shrimplesearch.di.modules.AppSubcomponentModule
import com.masterwok.shrimplesearch.di.modules.RepositoryModule
import com.masterwok.shrimplesearch.di.modules.ServiceModule
import com.masterwok.shrimplesearch.di.modules.ViewModelFactoryModule
import com.masterwok.shrimplesearch.features.about.di.AboutSubcomponent
import com.masterwok.shrimplesearch.features.query.di.QuerySubcomponent
import com.masterwok.shrimplesearch.features.settings.di.SettingsSubcomponent
import com.masterwok.shrimplesearch.features.splash.di.SplashSubcomponent
import com.masterwok.shrimplesearch.main.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ViewModelFactoryModule::class,
        AppSubcomponentModule::class,
        RepositoryModule::class,
        ServiceModule::class
    ]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun queryComponent(): QuerySubcomponent.Factory

    fun splashComponent(): SplashSubcomponent.Factory

    fun aboutComponent(): AboutSubcomponent.Factory

    fun settingsComponent(): SettingsSubcomponent.Factory

    fun inject(mainActivity: MainActivity)
}
