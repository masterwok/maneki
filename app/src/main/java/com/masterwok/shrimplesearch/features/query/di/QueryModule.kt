package com.masterwok.shrimplesearch.features.query.di

import androidx.lifecycle.ViewModel
import com.masterwok.shrimplesearch.common.AGGREGATE_INDEXER_ID
import com.masterwok.shrimplesearch.di.annotations.ViewModelKey
import com.masterwok.shrimplesearch.features.query.viewmodels.QueryViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named


@Module(includes = [QueryModule.Declarations::class])
class QueryModule {

    @Suppress("RedundantModalityModifier", "unused")
    @Module
    interface Declarations {
        @Binds
        @IntoMap
        @ViewModelKey(QueryViewModel::class)
        abstract fun bindViewModel(viewModel: QueryViewModel): ViewModel
    }

    @Provides
    @Named("aggregate_indexer_id")
    fun provideAggregateIndexerId(): String = AGGREGATE_INDEXER_ID

}