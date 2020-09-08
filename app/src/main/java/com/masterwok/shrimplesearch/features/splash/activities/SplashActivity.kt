package com.masterwok.shrimplesearch.features.splash.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.data.repositories.contracts.UserSettingsRepository
import com.masterwok.shrimplesearch.di.AppInjector
import com.masterwok.shrimplesearch.features.splash.models.BootstrapInfo
import com.masterwok.shrimplesearch.features.splash.viewmodels.SplashViewModel
import com.masterwok.shrimplesearch.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject
import kotlin.math.ceil


class SplashActivity : FragmentActivity() {

    @Inject
    lateinit var userSettingsRepository: UserSettingsRepository

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SplashViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppInjector
            .splashComponent
            .inject(this)

        setTheme(userSettingsRepository.getThemeId())

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        subscribeToLiveData()

        viewModel.initialize()
    }

    private fun subscribeToLiveData() {
        viewModel.liveDataBoostrapInfo.observe(this, this::configure)
        viewModel.liveDataBootStrapCompleted.observe(this) {
            startMainActivity()
        }
    }

    private fun configure(bootstrapInfo: BootstrapInfo) {
        progressBar.apply {
            progress = bootstrapInfo.initializedCount
            max = bootstrapInfo.totalIndexerCount
        }

        textViewIndexerProgressCount.text = getString(
            R.string.splash_progress,
            ceil((bootstrapInfo.initializedCount.toDouble() / bootstrapInfo.totalIndexerCount) * 100)
        )
    }

    private fun startMainActivity() {
        startActivity(MainActivity.createIntent(this).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })

        overridePendingTransition(0, 0)
    }

}
