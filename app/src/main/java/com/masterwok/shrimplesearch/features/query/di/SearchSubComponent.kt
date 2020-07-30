package com.masterwok.shrimplesearch.features.query.di

import com.masterwok.shrimplesearch.di.modules.ViewModelFactoryModule
import com.masterwok.shrimplesearch.features.query.fragments.QueryContainerFragment
import dagger.Subcomponent

@QueryScope
@Subcomponent(
    modules = [
        ViewModelFactoryModule::class,
        QueryModule::class
    ]
)
interface QuerySubcomponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): QuerySubcomponent
    }

    fun inject(fragment: QueryContainerFragment)

}