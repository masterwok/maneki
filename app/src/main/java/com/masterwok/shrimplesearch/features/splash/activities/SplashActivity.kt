package com.masterwok.shrimplesearch.features.splash.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.di.AppInjector
import com.masterwok.shrimplesearch.features.splash.models.BootstrapInfo
import com.masterwok.shrimplesearch.features.splash.viewmodels.SplashViewModel
import javax.inject.Inject


class SplashActivity : AppCompatActivity() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SplashViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppInjector
            .splashComponent
            .inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        subscribeToLiveData()

        viewModel.initialize()
    }

    private fun subscribeToLiveData() {
        viewModel.liveDataBoostrapInfo.observe(this, this::onBootstrapInfoChange)
        viewModel.liveDataBootStrapCompleted.observe(this) {
        }
    }

    private var highest = 0

    private fun onBootstrapInfoChange(bootstrapInfo: BootstrapInfo) {
        highest = if (bootstrapInfo.initializedCount > highest) {
            bootstrapInfo.initializedCount
        } else {
            highest
        }

    }


}
