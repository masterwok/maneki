package com.masterwok.shrimplesearch.features.splash.viewmodels

import androidx.lifecycle.ViewModel
import com.masterwok.shrimplesearch.common.data.repositories.contracts.JackettService
import javax.inject.Inject


class SplashViewModel @Inject constructor(
    private val jackettService: JackettService
) : ViewModel() {

}