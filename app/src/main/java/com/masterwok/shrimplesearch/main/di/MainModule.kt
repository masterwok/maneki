package com.masterwok.shrimplesearch.main.di

import androidx.lifecycle.ViewModel
import com.masterwok.shrimplesearch.di.annotations.ViewModelKey
import com.masterwok.shrimplesearch.main.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module(includes = [MainModule.Declarations::class])
class MainModule {

    @Suppress("RedundantModalityModifier", "unused")
    @Module
    interface Declarations {
        @Binds
        @IntoMap
        @ViewModelKey(MainActivityViewModel::class)
        abstract fun bindViewModel(viewModel: MainActivityViewModel): ViewModel
    }

}