package com.masterwok.shrimplesearch.features.about.fragments

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.masterwok.shrimplesearch.BuildConfig
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.constants.AnalyticEvent
import com.masterwok.shrimplesearch.common.data.services.contracts.AnalyticService
import com.masterwok.shrimplesearch.common.extensions.getPlayStoreUri
import com.masterwok.shrimplesearch.common.extensions.startPlayStoreActivity
import com.masterwok.shrimplesearch.common.utils.notNull
import com.masterwok.shrimplesearch.di.AppInjector
import com.masterwok.shrimplesearch.features.settings.fragments.SettingsFragment
import kotlinx.android.synthetic.main.fragment_about.*
import javax.inject.Inject

class AboutFragment : Fragment() {

    @Inject
    lateinit var analyticService: AnalyticService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(
        R.layout.fragment_about, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureVersionTextView()

        subscribeToViewComponents()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        AppInjector
            .aboutComponent
            .inject(this)
    }


    override fun onResume() {
        super.onResume()

        analyticService.logScreen(AboutFragment::class.java)
    }

    private fun subscribeToViewComponents() {
        buttonViewOnGitHub.setOnClickListener { openGitHubProjectUri() }
        buttonViewReview.setOnClickListener { openReviewPlayStore() }
        buttonViewShare.setOnClickListener { onShareButtonTapped() }
    }

    private fun onShareButtonTapped() = activity.notNull { activity ->
        ShareCompat
            .IntentBuilder
            .from(activity)
            .setType("text/plain")
            .setText(
                activity.getString(
                    R.string.share_text,
                    activity.getPlayStoreUri().toString()
                )
            )
            .startChooser()
    }

    private fun openGitHubProjectUri() = context.notNull { context ->
        val gitHubUri = Uri.parse(context.getString(R.string.gitHubUrl))

        try {
            startActivity(Intent(Intent.ACTION_VIEW, gitHubUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
        } catch (exception: ActivityNotFoundException) {
            analyticService.logException(exception, "No activity found to handle open GitHub Uri")
            presentUnableToOpenGitHubDialog()
        }
    }

    private fun openReviewPlayStore() = context.notNull { context ->
        try {
            context.startPlayStoreActivity()
        } catch (exception: ActivityNotFoundException) {
            analyticService.logException(exception, "No activity found to handle open GitHub Uri")
            presentUnableToOpenPlayStoreDialog()
        }
    }

    private fun presentUnableToOpenPlayStoreDialog() = context.notNull { context ->
        MaterialDialog(context).show {
            title(res = R.string.dialog_header_whoops)
            message(res = R.string.dialog_unable_to_open_play_store)
            positiveButton {
                title(res = R.string.button_ok)
            }
        }
    }

    private fun presentUnableToOpenGitHubDialog() = context.notNull { context ->
        MaterialDialog(context).show {
            title(res = R.string.dialog_header_whoops)
            message(res = R.string.dialog_unable_to_open_github_uri)
            positiveButton {
                title(res = R.string.button_ok)
            }
        }
    }

    private fun configureVersionTextView() = context.notNull { context ->
        textViewAboutVersion.text = context.getString(
            R.string.version,
            BuildConfig.VERSION_NAME
        )
    }

}