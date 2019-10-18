package io.github.zwieback.familyfinance.calculator.dialog

import android.content.Context
import android.content.DialogInterface
import android.util.TypedValue
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import io.github.zwieback.familyfinance.calculator.Operator
import io.github.zwieback.familyfinance.calculator.stateful.OnInvalidateStateListener
import io.github.zwieback.familyfinance.calculator.stateful.StatefulCalculator
import io.github.zwieback.familyfinance.core.R
import java.math.BigDecimal

class CalculatorDialog private constructor(
    context: Context,
    @StyleRes themeResId: Int,
    private val calculationResultListener: OnCalculationResultListener?,
    defaultValue: BigDecimal?
) : AlertDialog(context, resolveDialogTheme(context, themeResId)),
    DialogInterface.OnClickListener,
    OnInvalidateStateListener {

    private val calculator: StatefulCalculator
    private lateinit var numberEditText: TextInputEditText

    constructor(
        context: Context,
        calculationResultListener: OnCalculationResultListener?,
        defaultValue: BigDecimal?
    ) : this(context, 0, calculationResultListener, defaultValue)

    init {
        val themeContext = getContext()
        val view = View.inflate(themeContext, R.layout.dialog_calculator, null)
        setView(view)
        setButton(
            DialogInterface.BUTTON_POSITIVE,
            themeContext.getString(android.R.string.ok),
            this
        )
        setButton(
            DialogInterface.BUTTON_NEGATIVE,
            themeContext.getString(android.R.string.cancel),
            this
        )
        calculator = StatefulCalculator(this, defaultValue)
        setupViews(view)
        onInvalidateState()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE ->
                calculationResultListener?.let { listener ->
                    calculator.updateCalculator()
                    listener.onCalculationResult(calculator.calc())
                }
            DialogInterface.BUTTON_NEGATIVE -> cancel()
        }
    }

    private fun setupViews(view: View) {
        numberEditText = view.findViewById(R.id.number)
        setupButton(view, R.id.clr) { calculator.clear() }
        setupButton(view, R.id.del) { calculator.delete() }
        setupButton(view, R.id.eq) { calculator.eq() }
        setupButton(view, R.id.op_add) { calculator.changeOperator(Operator.ADD) }
        setupButton(view, R.id.op_sub) { calculator.changeOperator(Operator.SUB) }
        setupButton(view, R.id.op_mul) { calculator.changeOperator(Operator.MUL) }
        setupButton(view, R.id.op_div) { calculator.changeOperator(Operator.DIV) }
        setupButton(view, R.id.dec_point) { calculator.addDecimalSeparator() }
        setupButton(view, R.id.digit_0) { calculator.addDigit("0") }
        setupButton(view, R.id.digit_1) { calculator.addDigit("1") }
        setupButton(view, R.id.digit_2) { calculator.addDigit("2") }
        setupButton(view, R.id.digit_3) { calculator.addDigit("3") }
        setupButton(view, R.id.digit_4) { calculator.addDigit("4") }
        setupButton(view, R.id.digit_5) { calculator.addDigit("5") }
        setupButton(view, R.id.digit_6) { calculator.addDigit("6") }
        setupButton(view, R.id.digit_7) { calculator.addDigit("7") }
        setupButton(view, R.id.digit_8) { calculator.addDigit("8") }
        setupButton(view, R.id.digit_9) { calculator.addDigit("9") }
    }

    private fun setupButton(view: View, @IdRes viewId: Int, onClickListener: () -> Unit) {
        view.findViewById<View>(viewId).setOnClickListener { onClickListener() }
    }

    override fun onInvalidateState() {
        numberEditText.setText(calculator.buildOutput())
    }

    companion object {
        @StyleRes
        private fun resolveDialogTheme(context: Context, @StyleRes themeResId: Int): Int {
            return if (themeResId == 0) {
                val outValue = TypedValue()
                context.theme.resolveAttribute(R.attr.calculatorDialogTheme, outValue, true)
                outValue.resourceId
            } else {
                themeResId
            }
        }
    }
}
