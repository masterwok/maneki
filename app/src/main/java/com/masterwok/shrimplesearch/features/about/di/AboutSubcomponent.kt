package com.masterwok.shrimplesearch.features.about.di

import com.masterwok.shrimplesearch.features.about.fragments.AboutFragment
import dagger.Subcomponent

@AboutScope
@Subcomponent()
interface AboutSubcomponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): AboutSubcomponent
    }

    fun inject(fragment: AboutFragment)

}