package com.masterwok.shrimplesearch.di

import android.app.Application
import com.masterwok.shrimplesearch.di.components.AppComponent
import com.masterwok.shrimplesearch.di.components.DaggerAppComponent
import com.masterwok.shrimplesearch.main.MainActivity

class AppInjector {

    companion object {

        private lateinit var application: Application

        private val appComponent: AppComponent by lazy {
            DaggerAppComponent
                .factory()
                .create(application.applicationContext)
        }

        val queryComponent by lazy {
            appComponent.queryComponent().create()
        }

        val splashComponent by lazy {
            appComponent.splashComponent().create()
        }

        val aboutComponent by lazy {
            appComponent.aboutComponent().create()
        }

        val settingsComponent by lazy {
            appComponent.settingsComponent().create()
        }

        fun inject(mainActivity: MainActivity) = appComponent.inject(mainActivity)

        fun init(application: Application) {
            this.application = application
        }

    }
}

