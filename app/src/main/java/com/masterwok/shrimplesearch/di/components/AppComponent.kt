package com.masterwok.shrimplesearch.di.components

import android.content.Context
import com.masterwok.shrimplesearch.di.modules.AppSubcomponentModule
import com.masterwok.shrimplesearch.di.modules.ServiceModule
import com.masterwok.shrimplesearch.di.modules.ViewModelFactoryModule
import com.masterwok.shrimplesearch.features.query.di.QuerySubcomponent
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

    fun inject(mainActivity: MainActivity)
}
