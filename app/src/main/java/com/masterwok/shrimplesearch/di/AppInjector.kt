package com.masterwok.shrimplesearch.di

import android.app.Application
import com.masterwok.shrimplesearch.di.components.AppComponent
import com.masterwok.shrimplesearch.di.components.DaggerAppComponent

class AppInjector {

    companion object {

        private lateinit var application: Application

        private val appComponent: AppComponent by lazy {
            DaggerAppComponent
                .factory()
                .create(application.applicationContext)
        }

        val searchComponent by lazy {
            appComponent.searchComponent().create()
        }

        val splashComponent by lazy {
            appComponent.splashComponent().create()
        }

        fun init(application: Application) {
            this.application = application
        }

    }
}

