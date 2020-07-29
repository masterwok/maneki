package com.masterwok.shrimplesearch.features.splash.di

import androidx.lifecycle.ViewModel
import com.masterwok.shrimplesearch.di.annotations.ViewModelKey
import com.masterwok.shrimplesearch.features.search.viewmodels.SearchViewModel
import com.masterwok.shrimplesearch.features.splash.viewmodels.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class SplashModule {

    @SplashScope
    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindViewModel(viewModel: SplashViewModel): ViewModel

}