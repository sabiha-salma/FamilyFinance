package io.github.zwieback.familyfinance.widget

import android.content.Context
import android.text.InputFilter
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import io.github.zwieback.familyfinance.calculator.dialog.OnCalculationResultListener
import io.github.zwieback.familyfinance.core.R
import io.github.zwieback.familyfinance.extension.parseAsBigDecimal
import io.github.zwieback.familyfinance.extension.toStringOrEmpty
import io.github.zwieback.familyfinance.util.DialogUtils
import io.github.zwieback.familyfinance.widget.filter.DecimalNumberInputFilter
import java.math.BigDecimal

/**
 * View to input decimal numbers and (optional) only one delimiter
 */
class DecimalNumberTextEdit @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.attr.editTextStyle
) : IconicsEditText(context, attrs, defStyle),
    OnCalculationResultListener {

    override val iconName: String
        get() = "cmd_calculator"

    override val iconSize: Int
        get() = R.dimen.calculator_icon_size

    override val iconColor: Int
        get() = R.color.icon_inside_edit_text

    override fun initialize(context: Context) {
        super.initialize(context)
        inputType = EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
        keyListener = DigitsKeyListener.getInstance("0123456789.,")
        filters = arrayOf<InputFilter>(DecimalNumberInputFilter())
        setIconVisible(true)
    }

    override fun onIconClick() {
        DialogUtils.showCalculatorDialog(
            context,
            this,
            this.text?.toString()?.parseAsBigDecimal()
        )
    }

    override fun onCalculationResult(result: BigDecimal?) {
        setText(result.toStringOrEmpty())
    }
}
