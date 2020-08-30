package com.masterwok.shrimplesearch.features.query.components

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.masterwok.shrimplesearch.R

class SortQueryResultsComponent : ConstraintLayout {

    constructor(context: Context) : super(context) {
        inflate(null)
        onFinishInflate()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        inflate(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        inflate(attrs)
    }

    private fun inflate(attrs: AttributeSet?) {
        inflate(context, R.layout.component_sort_query_results, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        setOnTouchListener(null)
    }

}