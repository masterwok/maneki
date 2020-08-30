package com.masterwok.shrimplesearch.features.query.components

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import com.afollestad.materialdialogs.utils.MDUtil.getStringArray
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.contracts.Configurable
import com.masterwok.shrimplesearch.common.extensions.dpToPx
import kotlinx.android.synthetic.main.component_sort_by.view.*

class SortByComponent : ConstraintLayout, Configurable<SortByComponent.Model> {

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
        inflate(context, R.layout.component_sort_by, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        setOnTouchListener(null)
    }


    override fun configure(model: Model) {
        configureSortPills(model.sortPills, model.selectedSortPill)
    }

    private fun configureSortPills(sortPills: List<Pill>, selectedSortPill: Pill) {
        sortPills.forEach { pill ->
            val checkBox = AppCompatCheckBox(context).apply {
                setBackgroundResource(R.drawable.background_pill)
                setPadding(8.dpToPx(context))
                typeface = ResourcesCompat.getFont(context, R.font.eina_03_regular)
                text = context.getString(pill.title)
                isSelected = pill == selectedSortPill
                gravity = Gravity.CENTER
                buttonDrawable = null
                minimumHeight = 0
                minimumWidth = 0
            }

            flexboxLayoutSort.addView(checkBox)
        }
    }


    data class Model(
        val sortPills: List<Pill>,
        val orderPills: List<Pill>,
        val selectedSortPill: Pill,
        val selectedOrderPill: Pill
    )

    data class Pill(@StringRes val title: Int)


}