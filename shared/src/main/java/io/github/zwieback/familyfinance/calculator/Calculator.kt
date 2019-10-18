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
        if (leftOperand == null) {
            leftOperand = BigDecimal.ZERO
        }
        return try {
            operator?.calc(leftOperand!!, rightOperand!!)
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
