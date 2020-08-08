package com.masterwok.shrimplesearch.features.query.di

import com.masterwok.shrimplesearch.features.query.fragments.IndexerQueryResultsFragment
import com.masterwok.shrimplesearch.features.query.fragments.QueryContainerFragment
import com.masterwok.shrimplesearch.features.query.fragments.QueryFragment
import dagger.Subcomponent

@QueryScope
@Subcomponent(
    modules = [
        QueryModule::class
    ]
)
interface QuerySubcomponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): QuerySubcomponent
    }

    fun inject(fragment: QueryContainerFragment)
    fun inject(fragment: QueryFragment)
    fun inject(fragment: IndexerQueryResultsFragment)

}