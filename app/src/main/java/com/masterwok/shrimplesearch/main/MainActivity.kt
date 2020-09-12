package com.masterwok.shrimplesearch.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.constants.AnalyticEvent
import com.masterwok.shrimplesearch.common.data.repositories.contracts.UserSettingsRepository
import com.masterwok.shrimplesearch.common.data.services.contracts.AnalyticService
import com.masterwok.shrimplesearch.di.AppInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_toolbar_maneki.*
import kotlinx.android.synthetic.main.view_dialog_exit.view.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var analyticService: AnalyticService

    @Inject
    lateinit var userSettingsRepository: UserSettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        AppInjector.inject(this)

        setTheme(userSettingsRepository.getThemeId())

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initSupportActionBar()
        initNavigation()
    }

    private fun initSupportActionBar() {
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayShowHomeEnabled(false)
        }
    }

    private val navController by lazy {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentNavHost) as NavHostFragment

        navHostFragment.navController
    }

    private fun initNavigation() {
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        toolbar.setupWithNavController(navController, appBarConfiguration)

        navigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.aboutFragment -> analyticService.logEvent(AnalyticEvent.MenuItemAboutTapped)
            }
        }
    }

    private val isExitDialogEnabled
        get() = userSettingsRepository
            .read()
            .isExistDialogEnabled

    override fun onBackPressed() = when (navController.graph.startDestination) {
        navController.currentDestination?.id -> {
            if (isExitDialogEnabled) {
                presentQuitAppDialog()
            } else {
                finishAndRemoveTask()
            }
        }
        else -> super.onBackPressed()
    }

    private fun presentQuitAppDialog() {
        MaterialDialog(this).show {
            customView(R.layout.view_dialog_exit)
            positiveButton(res = R.string.dialog_exit) {
                if (getCustomView().checkBoxDontAskAgain.isChecked) {
                    disableExitDialog()
                }

                finishAndRemoveTask()
            }
            negativeButton(res = R.string.dialog_cancel)
        }
    }

    private fun disableExitDialog() = userSettingsRepository.update(
        userSettingsRepository
            .read()
            .copy(isExistDialogEnabled = false)
    )

    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
