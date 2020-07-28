package com.masterwok.shrimplesearch

import android.app.Application
import com.masterwok.shrimplesearch.di.AppInjector

class Maneki : Application() {

    override fun onCreate() {
        super.onCreate()

        AppInjector.init(this)
    }
}