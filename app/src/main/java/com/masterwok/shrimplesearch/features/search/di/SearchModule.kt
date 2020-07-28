package com.masterwok.shrimplesearch.features.search.di

import androidx.lifecycle.ViewModel
import com.masterwok.shrimplesearch.di.annotations.ViewModelKey
import com.masterwok.shrimplesearch.features.search.viewmodels.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class SearchModule {

    @SearchScope
    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindViewModel(viewModel: SearchViewModel): ViewModel

}