package com.masterwok.shrimplesearch.common.extensions

import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

fun AppBarLayout.disableScroll(toolbar: Toolbar) {
    toolbar.layoutParams = (toolbar.layoutParams as AppBarLayout.LayoutParams).apply {
        scrollFlags = 0
    }

    layoutParams = (layoutParams as CoordinatorLayout.LayoutParams).apply {
        behavior = null
    }
}

fun AppBarLayout.enableScroll(toolbar: Toolbar, scrollFlags: Int) {
    toolbar.layoutParams = (toolbar.layoutParams as AppBarLayout.LayoutParams).apply {
        this.scrollFlags = scrollFlags
    }

    layoutParams = (layoutParams as CoordinatorLayout.LayoutParams).apply {
        behavior = AppBarLayout.Behavior()
    }
}