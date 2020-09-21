package com.masterwok.shrimplesearch.main.di

import com.masterwok.shrimplesearch.main.MainActivity
import dagger.Subcomponent

@MainScope
@Subcomponent(modules = [MainModule::class])
interface MainSubcomponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainSubcomponent
    }

    fun inject(mainActivity: MainActivity)
}