package com.masterwok.shrimplesearch.di.modules

import com.masterwok.shrimplesearch.features.search.di.SearchSubcomponent
import dagger.Module

@Module(
    subcomponents = [
        SearchSubcomponent::class
    ]
)
class AppSubcomponentModule