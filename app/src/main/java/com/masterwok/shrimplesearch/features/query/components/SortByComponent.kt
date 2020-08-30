package com.masterwok.shrimplesearch.features.query.components

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.CompoundButton
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.core.view.setPadding
import com.google.android.flexbox.FlexboxLayout
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

    @Suppress("UNUSED_PARAMETER")
    private fun inflate(attrs: AttributeSet?) {
        inflate(context, R.layout.component_sort_by, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        setOnTouchListener(null)
    }

    override fun configure(model: Model) {
        configureFlexBoxPills(flexboxLayoutSort, model.sortPills, model.selectedSortPill)
        configureFlexBoxPills(flexboxLayoutOrder, model.orderPills, model.selectedOrderPill)
    }

    private fun configureFlexBoxPills(
        flexBoxLayout: FlexboxLayout,
        sortPills: List<Pill>,
        selectedSortPill: Pill
    ) = sortPills.forEach { pill ->
        flexBoxLayout.addView(
            createPillRadioButton(
                flexBoxLayout,
                pill.title,
                pill == selectedSortPill
            )
        )
    }

    private fun createPillRadioButton(
        flexBoxLayout: FlexboxLayout,
        @StringRes pillTitle: Int,
        isChecked: Boolean
    ): AppCompatRadioButton = AppCompatRadioButton(context).apply {
        this.isChecked = isChecked
        setBackgroundResource(R.drawable.background_pill)
        setPadding(8.dpToPx(context))
        tag = pillTitle
        typeface = ResourcesCompat.getFont(context, R.font.eina_03_regular)
        text = context.getString(pillTitle)
        gravity = Gravity.CENTER
        buttonDrawable = null
        minimumHeight = 0
        minimumWidth = 0

        setOnCheckedChangeListener { button, isChecked ->
            setFlexBoxPillCheckStates(flexBoxLayout, button.tag, isChecked)
        }
    }

    private fun setFlexBoxPillCheckStates(
        flexBoxLayout: FlexboxLayout,
        buttonTag: Any,
        isChecked: Boolean
    ) {
        if (!isChecked) {
            return
        }

        flexBoxLayout
            .children
            .filterIsInstance<CompoundButton>()
            .filterNot { it.tag == buttonTag }
            .forEach { it.isChecked = false }
    }

    data class Model(
        val sortPills: List<Pill>,
        val orderPills: List<Pill>,
        val selectedSortPill: Pill,
        val selectedOrderPill: Pill
    )

    data class Pill(@StringRes val title: Int)

}