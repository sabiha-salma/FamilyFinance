package io.github.zwieback.familyfinance.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.widget.TextViewCompat

import com.google.android.material.textfield.TextInputEditText
import com.mikepenz.iconics.IconicsColor
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.IconicsSize

import io.github.zwieback.familyfinance.core.R
import io.github.zwieback.familyfinance.util.ViewUtils
import io.github.zwieback.familyfinance.widget.listener.OnIconClickListener

/**
 * View with an ability to display custom icon at the end of this view's border
 */
abstract class IconicsEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyle),
    View.OnTouchListener,
    OnIconClickListener {

    private lateinit var icon: IconicsDrawable
    private var iconVisible: Boolean = false

    protected abstract val iconName: String

    @get:DimenRes
    protected abstract val iconSize: Int

    @get:ColorRes
    protected abstract val iconColor: Int

    override fun onFinishInflate() {
        super.onFinishInflate()
        initialize(context)
    }

    @CallSuper
    protected open fun initialize(context: Context) {
        icon = createIcon(context)
        setIconVisible(false)
        setOnTouchListener(this)
    }

    private fun createIcon(context: Context): IconicsDrawable {
        return IconicsDrawable(context, iconName)
            .size(IconicsSize.res(iconSize))
            .color(IconicsColor.colorRes(iconColor))
    }

    private fun repaintIcon(icon: Drawable?) {
        iconVisible = icon != null
        val drawables = TextViewCompat.getCompoundDrawablesRelative(this)
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(
            this,
            drawables[0], drawables[1], icon, drawables[3]
        )
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (isClickedOnIcon(event)) {
            onIconClick()
            return true
        }
        return false
    }

    private fun isClickedOnIcon(event: MotionEvent): Boolean {
        if (event.action != MotionEvent.ACTION_UP) {
            return false
        }
        val ltr = ViewUtils.isLeftToRightLayoutDirection(this)
        val x = event.x.toInt()
        val y = event.y.toInt()
        val left = if (ltr) width - paddingRight - icon.intrinsicWidth else 0
        val right = if (ltr) width else paddingLeft + icon.intrinsicWidth
        return x in left..right && y in 0..(bottom - top)
    }

    protected fun setIconVisible(visible: Boolean) {
        if (!isEnabled) {
            return
        }
        val wasVisible = iconVisible
        if (visible != wasVisible) {
            repaintIcon(if (visible) icon else null)
        }
    }
}
