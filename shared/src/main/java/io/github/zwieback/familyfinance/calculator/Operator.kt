package io.github.zwieback.familyfinance.calculator

import java.math.BigDecimal
import java.math.RoundingMode

enum class Operator(val opChar: String) {

    ADD("+") {
        override fun calc(leftOperand: BigDecimal, rightOperand: BigDecimal): BigDecimal {
            return leftOperand.add(rightOperand)
        }
    },
    SUB("-") {
        override fun calc(leftOperand: BigDecimal, rightOperand: BigDecimal): BigDecimal {
            return leftOperand.subtract(rightOperand)
        }
    },
    MUL("×") {
        override fun calc(leftOperand: BigDecimal, rightOperand: BigDecimal): BigDecimal {
            return leftOperand.multiply(rightOperand)
        }
    },
    DIV("÷") {
        override fun calc(leftOperand: BigDecimal, rightOperand: BigDecimal): BigDecimal {
            return leftOperand.divide(rightOperand, RoundingMode.HALF_EVEN)
        }
    };

    abstract fun calc(leftOperand: BigDecimal, rightOperand: BigDecimal): BigDecimal
}
