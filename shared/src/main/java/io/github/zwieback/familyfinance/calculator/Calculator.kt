package io.github.zwieback.familyfinance.calculator

import java.math.BigDecimal

class Calculator {

    private var leftOperand: BigDecimal? = null
    private var rightOperand: BigDecimal? = null
    private var operator: Operator? = null

    fun calc(): BigDecimal? {
        if (operator == null || rightOperand == null) {
            return leftOperand
        }
        val rightValue = rightOperand ?: BigDecimal.ZERO
        val leftValue = leftOperand ?: BigDecimal.ZERO
        return try {
            operator?.calc(leftValue, rightValue)
        } catch (ignored: ArithmeticException) {
            null
        }
    }

    fun setLeftOperand(leftOperand: BigDecimal?) {
        this.leftOperand = leftOperand
    }

    fun setRightOperand(rightOperand: BigDecimal?) {
        this.rightOperand = rightOperand
    }

    fun setOperator(operator: Operator?) {
        this.operator = operator
    }
}
