package com.masterwok.shrimplesearch.common.utils

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.features.query.components.SortComponent

object DialogUtil {

    fun presentSortDialog(
        context: Context,
        sortComponentModel: SortComponent.Model,
        onDialogDismiss: (SortComponent.Model) -> Unit
    ) {
        val sortComponent = SortComponent(context).apply {
            configure(sortComponentModel)
        }

        MaterialDialog(context).show {
            customView(view = sortComponent)
            positiveButton {
                title(res = R.string.button_done)
                onDialogDismiss(sortComponent.getModel())
            }
            negativeButton {
                title(res = R.string.button_cancel)
            }
        }
    }
}
