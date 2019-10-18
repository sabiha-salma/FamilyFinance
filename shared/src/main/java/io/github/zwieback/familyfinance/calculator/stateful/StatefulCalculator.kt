package io.github.zwieback.familyfinance.calculator.stateful

import io.github.zwieback.familyfinance.calculator.Calculator
import io.github.zwieback.familyfinance.calculator.Operator
import io.github.zwieback.familyfinance.util.NumberUtils
import io.github.zwieback.familyfinance.util.StringUtils
import io.github.zwieback.familyfinance.util.StringUtils.EMPTY
import java.math.BigDecimal

class StatefulCalculator(
    private val invalidateListener: OnInvalidateStateListener?,
    defaultValue: BigDecimal?
) {

    private val decimalSeparator: String = NumberUtils.decimalSeparator
    private var leftOperand: String
    private lateinit var rightOperand: String
    private var operator: Operator? = null
    private val calculator: Calculator = Calculator()
    private lateinit var state: InputState

    init {
        reset()
        leftOperand = NumberUtils.bigDecimalToString(defaultValue)
    }

    fun clear() {
        reset()
        invalidate()
    }

    fun delete() {
        when (state) {
            InputState.INPUT_LEFT_OPERAND -> {
                leftOperand = StringUtils.deleteLastChar(leftOperand)
            }
            InputState.INPUT_RIGHT_OPERAND -> {
                rightOperand = StringUtils.deleteLastChar(rightOperand)
                if (rightOperand.isEmpty()) {
                    state = InputState.INPUT_LEFT_OPERAND
                    operator = null
                }
            }
        }
        invalidate()
    }

    fun eq() {
        updateCalculator()
        reset()
        leftOperand = NumberUtils.bigDecimalToString(calculator.calc())
        invalidate()
    }

    fun changeOperator(operator: Operator) {
        this.operator = operator
        this.state = InputState.INPUT_RIGHT_OPERAND
        invalidate()
    }

    fun addDecimalSeparator() {
        when (state) {
            InputState.INPUT_LEFT_OPERAND -> {
                leftOperand = StringUtils.addUniqueChar(leftOperand, decimalSeparator)
            }
            InputState.INPUT_RIGHT_OPERAND -> {
                rightOperand = StringUtils.addUniqueChar(rightOperand, decimalSeparator)
            }
        }
        invalidate()
    }

    fun addDigit(digit: String) {
        when (state) {
            InputState.INPUT_LEFT_OPERAND -> {
                leftOperand = StringUtils.addChar(leftOperand, digit)
            }
            InputState.INPUT_RIGHT_OPERAND -> {
                rightOperand = StringUtils.addChar(rightOperand, digit)
            }
        }
        invalidate()
    }

    fun updateCalculator() {
        calculator.setLeftOperand(NumberUtils.stringToBigDecimal(leftOperand))
        calculator.setRightOperand(NumberUtils.stringToBigDecimal(rightOperand))
        calculator.setOperator(operator)
    }

    fun buildOutput(): String {
        val result = StringBuilder()
        if (leftOperand.isNotEmpty()) {
            result.append(leftOperand)
        }
        operator?.let {
            result.append(" ${it.opChar} ")
        }
        if (rightOperand.isNotEmpty()) {
            result.append(rightOperand)
        }
        return result.toString()
    }

    fun calc(): BigDecimal? {
        return calculator.calc()
    }

    private fun reset() {
        leftOperand = EMPTY
        rightOperand = EMPTY
        operator = null
        state = InputState.INPUT_LEFT_OPERAND
    }

    private fun invalidate() {
        invalidateListener?.onInvalidateState()
    }
}
