package io.github.zwieback.familyfinance.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import io.github.zwieback.familyfinance.core.R
import io.github.zwieback.familyfinance.util.StringUtils.EMPTY
import io.github.zwieback.familyfinance.widget.adapter.TextWatcherAdapter
import io.github.zwieback.familyfinance.widget.listener.OnClearTextListener
import io.github.zwieback.familyfinance.widget.listener.TextWatcherListener

/**
 * Read-only view with an ability to clear the current text
 */
class ClearableEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.attr.editTextStyle
) : IconicsEditText(context, attrs, defStyle),
    TextWatcherListener,
    View.OnFocusChangeListener {

    private var onClearTextListener: OnClearTextListener? = null

    override val iconName: String
        get() = "faw_times_circle"

    override val iconSize: Int
        get() = R.dimen.clear_icon_size

    override val iconColor: Int
        get() = R.color.icon_inside_edit_text

    override fun initialize(context: Context) {
        super.initialize(context)
        addTextChangedListener(TextWatcherAdapter(this))
    }

    override fun onIconClick() {
        setText(EMPTY)
    }

    override fun onTextChanged(text: String) {
        if (isFocusable) {
            if (isFocused) {
                setIconVisible(text.isNotEmpty())
            }
        } else {
            setIconVisible(text.isNotEmpty())
        }
        if (text.isEmpty()) {
            onClearTextListener?.invoke()
        }
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        setIconVisible(hasFocus && !text.isNullOrEmpty())
    }

    /**
     * See [Java SAM vs. Kotlin Function Types](https://medium.com/tompee/idiomatic-kotlin-lambdas-and-sam-constructors-fe2075965bfb)
     * paragraph why required to use a function type rather than an interface.
     */
    fun setOnClearTextListener(onClearTextListener: OnClearTextListener?) {
        this.onClearTextListener = onClearTextListener
    }
}
