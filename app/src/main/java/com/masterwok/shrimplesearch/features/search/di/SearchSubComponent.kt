package com.masterwok.shrimplesearch.features.search.di

import com.masterwok.shrimplesearch.di.modules.ViewModelFactoryModule
import com.masterwok.shrimplesearch.features.search.fragments.SearchFragment
import dagger.Subcomponent

@SearchScope
@Subcomponent(
    modules = [
        ViewModelFactoryModule::class,
        SearchModule::class
    ]
)
interface SearchSubcomponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): SearchSubcomponent
    }

    fun inject(fragment: SearchFragment)

}