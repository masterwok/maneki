package com.masterwok.shrimplesearch.common.extensions

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManager


fun ReviewManager.attemptToPresentInAppReview(
    activity: FragmentActivity,
    onCompletionAction: () -> Unit
) {
    activity.lifecycleScope.launchWhenResumed {
        val reviewInfo = requestReview()

        launchReviewFlow(activity, reviewInfo).addOnCompleteListener { onCompletionAction() }
    }
}
