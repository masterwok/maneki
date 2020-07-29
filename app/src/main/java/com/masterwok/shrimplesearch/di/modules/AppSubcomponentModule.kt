package com.masterwok.shrimplesearch.di.modules

import com.masterwok.shrimplesearch.features.search.di.SearchSubcomponent
import com.masterwok.shrimplesearch.features.splash.di.SplashSubcomponent
import dagger.Module

@Module(
    subcomponents = [
        SearchSubcomponent::class
        , SplashSubcomponent::class
    ]
)
class AppSubcomponentModule