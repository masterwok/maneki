package com.masterwok.shrimplesearch.common.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.extensions.hideSoftKeyboard

class ClearableAutoCompleteTextView : AppCompatAutoCompleteTextView {

    private var onTextClearedListener: (() -> Unit)? = null

    constructor(context: Context) : super(context) {
        onFinishInflate()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onFinishInflate() {
        super.onFinishInflate()

        setOnTouchListener(null)
    }

    override fun setOnFocusChangeListener(l: OnFocusChangeListener?) {
        super.setOnFocusChangeListener { view, isFocused ->
            setDrawableRight()
            l?.onFocusChange(view, isFocused)
        }
    }

    override fun setOnEditorActionListener(l: OnEditorActionListener?) {
        super.setOnEditorActionListener { view, actionId, keyEvent ->
            setDrawableRight()
            l?.onEditorAction(view, actionId, keyEvent) ?: false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setOnTouchListener(l: OnTouchListener?) {
        super.setOnTouchListener { view, motionEvent ->
            val drawableRight = compoundDrawables[2]

            if (
                drawableRight != null
                && motionEvent.action == MotionEvent.ACTION_UP
                && motionEvent.x >= width - totalPaddingRight
            ) {
                text = null
                setDrawableRight()
                onTextClearedListener?.invoke()
                (context as? Activity)?.hideSoftKeyboard()
                true
            } else {
                l?.onTouch(view, motionEvent) ?: false
            }

        }
    }

    private fun setDrawableRight() = setCompoundDrawablesWithIntrinsicBounds(
        null, null, if (text.isNullOrEmpty()) {
            null
        } else {
            ContextCompat.getDrawable(context, R.drawable.ic_auto_complete_clear)
        }, null
    )

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)

        setDrawableRight()
    }

    fun setOnTextClearedListener(listener: () -> Unit) {
        onTextClearedListener = listener
    }

}