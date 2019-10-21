package io.github.zwieback.familyfinance.core.adapter

import android.text.InputType.*
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.TextViewBindingAdapter
import io.github.zwieback.familyfinance.R

/**
 * @see [TextViewBindingAdapter]
 */
object TextViewBindingAdapterEx {

    /**
     * Make TextView not editable and move text to next line if text is long.
     *
     * @param view     which view make not editable
     * @param readOnly `true` made view not editable
     *
     * See [How to make EditText not editable through XML in Android?](https://stackoverflow.com/a/7853003/8035065)
     */
    @JvmStatic
    @BindingAdapter("readOnly")
    fun setReadOnly(view: TextView, readOnly: Boolean) {
        view.isCursorVisible = !readOnly
        view.isFocusable = !readOnly
        view.isFocusableInTouchMode = !readOnly
        if (readOnly) {
            view.setTag(R.id.inputType, view.inputType)
            if (view.inputType == TYPE_NULL) {
                view.setRawInputType(TYPE_CLASS_TEXT or TYPE_TEXT_FLAG_MULTI_LINE)
            }
        } else {
            val inputType = view.getTag(R.id.inputType)
            if (inputType is Int) {
                view.setRawInputType(inputType)
            }
        }
    }
}
