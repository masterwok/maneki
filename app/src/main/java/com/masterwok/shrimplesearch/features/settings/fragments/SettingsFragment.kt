package com.masterwok.shrimplesearch.features.settings.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.constants.Theme
import com.masterwok.shrimplesearch.common.data.models.UserSettings
import com.masterwok.shrimplesearch.common.data.services.contracts.AnalyticService
import com.masterwok.shrimplesearch.di.AppInjector
import com.masterwok.shrimplesearch.features.settings.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment : Fragment() {

    @Inject
    lateinit var analyticService: AnalyticService

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    private val currentSettings get() = checkNotNull(viewModel.liveDataUserSettings.value)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(
        R.layout.fragment_settings,
        container,
        false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.liveDataUserSettings.observe(viewLifecycleOwner, this::configure)
    }

    override fun onResume() {
        super.onResume()

        analyticService.logScreen(SettingsFragment::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        AppInjector.settingsComponent.inject(this)
    }

    private fun configure(userSettings: UserSettings) {
        unsubscribeFromViewComponents()
        configureThemeSelection(userSettings.theme)
        configureSwitches(userSettings)
        subscribeToViewComponents()
    }

    private fun configureSwitches(userSettings: UserSettings) {
        switchScrollToTop. isChecked = checkNotNull(userSettings.isScrollToTopNotificationsEnabled)
        switchMagnet. isChecked = checkNotNull(userSettings.isOnlyMagnetQueryResultItemsEnabled)
    }

    private fun configureThemeSelection(theme: Theme): Unit = when (theme) {
        Theme.Light -> radioButtonThemeLight.isChecked = true
        Theme.Oled -> radioButtonThemeOled.isChecked = true
    }

    private fun unsubscribeFromViewComponents() {
        switchScrollToTop.setOnCheckedChangeListener(null)
        switchMagnet.setOnCheckedChangeListener(null)
    }

    private fun subscribeToViewComponents() {
        subscribeToThemeRadioGroup()
        subscribeToScrollToTopNotificationsSwitch()
    }

    private fun subscribeToScrollToTopNotificationsSwitch() {
        switchScrollToTop.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateUserSettings(
                currentSettings.copy(isScrollToTopNotificationsEnabled = isChecked)
            )
        }
        switchMagnet.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateUserSettings(
                currentSettings.copy(isOnlyMagnetQueryResultItemsEnabled = isChecked)
            )
        }
    }

    private fun subscribeToThemeRadioGroup() = radioGroupTheme.setOnCheckedChangeListener { _, _ ->
        val oldSettings = checkNotNull(viewModel.liveDataUserSettings.value)
        val selectedThemeId: Int

        val userSettings = oldSettings.copy(
            theme = when (radioGroupTheme.checkedRadioButtonId) {
                R.id.radioButtonThemeLight -> {
                    selectedThemeId = R.style.AppTheme
                    Theme.Light
                }
                R.id.radioButtonThemeOled -> {
                    selectedThemeId = R.style.AppTheme_Oled
                    Theme.Oled
                }
                else -> error("Theme not registered on settings.")
            }
        )

        viewModel.updateUserSettings(userSettings.copy())

        activity?.setTheme(selectedThemeId)
        activity?.recreate()
    }

}