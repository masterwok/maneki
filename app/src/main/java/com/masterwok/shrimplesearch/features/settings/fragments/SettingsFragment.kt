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
import com.masterwok.shrimplesearch.di.AppInjector
import com.masterwok.shrimplesearch.features.settings.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(
        R.layout.fragment_settings, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configure(viewModel.readUserSettings())

        subscribeToViewComponents()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        AppInjector.settingsComponent.inject(this)
    }

    private fun configure(userSettings: UserSettings) {
        configureThemeSelection(userSettings.theme)

        switchScrollToTop.isChecked = checkNotNull(userSettings.isScrollToTopNotificationsEnabled)
    }

    private fun configureThemeSelection(theme: Theme): Unit = when (theme) {
        Theme.Light -> radioButtonThemeLight.isChecked = true
        Theme.Oled -> radioButtonThemeOled.isChecked = true
    }

    private fun subscribeToViewComponents() {
        subscribeToThemeRadioGroup()
        subscribeToScrollToTopNotificationsSwitch()
    }

    private fun subscribeToScrollToTopNotificationsSwitch() {
        switchScrollToTop.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateUserSettings(
                viewModel.readUserSettings().copy(
                    isScrollToTopNotificationsEnabled = isChecked
                )
            )
        }
    }

    private fun subscribeToThemeRadioGroup() = radioGroupTheme.setOnCheckedChangeListener { _, _ ->
        val selectedThemeId: Int

        val userSettings = viewModel
            .readUserSettings()
            .copy(
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