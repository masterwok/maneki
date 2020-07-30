package com.masterwok.shrimplesearch.features.query.di

import androidx.lifecycle.ViewModel
import com.masterwok.shrimplesearch.di.annotations.ViewModelKey
import com.masterwok.shrimplesearch.features.query.viewmodels.QueryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class QueryModule {

    @QueryScope
    @Binds
    @IntoMap
    @ViewModelKey(QueryViewModel::class)
    abstract fun bindViewModel(viewModel: QueryViewModel): ViewModel

}