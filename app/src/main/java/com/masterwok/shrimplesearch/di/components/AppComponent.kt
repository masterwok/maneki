package com.masterwok.shrimplesearch.di.components

import android.content.Context
import com.masterwok.shrimplesearch.di.modules.AppSubcomponentModule
import com.masterwok.shrimplesearch.di.modules.ServiceModule
import com.masterwok.shrimplesearch.features.search.di.SearchSubcomponent
import com.masterwok.shrimplesearch.main.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppSubcomponentModule::class,
        ServiceModule::class
    ]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun searchComponent(): SearchSubcomponent.Factory

    fun inject(mainActivity: MainActivity)
}
