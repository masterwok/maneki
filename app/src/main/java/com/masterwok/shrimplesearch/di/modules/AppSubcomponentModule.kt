package com.masterwok.shrimplesearch.di.modules

import com.masterwok.shrimplesearch.features.query.di.QuerySubcomponent
import com.masterwok.shrimplesearch.features.splash.di.SplashSubcomponent
import dagger.Module

@Module(
    subcomponents = [
        QuerySubcomponent::class
        , SplashSubcomponent::class
    ]
)
class AppSubcomponentModule