package io.github.zwieback.familyfinance.calculator.dialog

import java.math.BigDecimal

interface OnCalculationResultListener {

    fun onCalculationResult(result: BigDecimal?)
}
