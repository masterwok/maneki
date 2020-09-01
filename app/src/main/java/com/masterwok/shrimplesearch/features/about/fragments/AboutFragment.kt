package com.masterwok.shrimplesearch.features.about.fragments

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.masterwok.shrimplesearch.BuildConfig
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.constants.AnalyticEvent
import com.masterwok.shrimplesearch.common.data.services.contracts.AnalyticService
import com.masterwok.shrimplesearch.common.utils.notNull
import com.masterwok.shrimplesearch.di.AppInjector
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

    private fun subscribeToViewComponents() {
        buttonViewOnGitHub.setOnClickListener { openGitHubProjectUri() }
    }

    private fun openGitHubProjectUri() = context.notNull { context ->
        val gitHubUri = Uri.parse(context.getString(R.string.gitHubUrl))

        try {
            startActivity(Intent(Intent.ACTION_VIEW, gitHubUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
        } catch (exception: ActivityNotFoundException) {
            analyticService.logException(exception, "No activity found to handle open GitHub Uri")
        }
    }

    private fun configureVersionTextView() = context.notNull { context ->
        textViewAboutVersion.text = context.getString(
            R.string.version,
            BuildConfig.VERSION_NAME
        )
    }

}