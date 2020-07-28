package com.masterwok.shrimplesearch.di.modules

import androidx.lifecycle.ViewModelProvider
import com.masterwok.shrimplesearch.di.factories.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory

}