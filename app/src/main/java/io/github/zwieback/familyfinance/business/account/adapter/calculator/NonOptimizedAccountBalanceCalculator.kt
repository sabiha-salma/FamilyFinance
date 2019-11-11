package io.github.zwieback.familyfinance.business.account.adapter.calculator

import io.github.zwieback.familyfinance.core.model.AccountView
import io.github.zwieback.familyfinance.core.model.ExchangeRate
import io.github.zwieback.familyfinance.core.model.Operation
import io.github.zwieback.familyfinance.core.model.type.OperationType
import io.github.zwieback.familyfinance.util.BigDecimalConverterUtils
import io.reactivex.functions.Consumer
import io.requery.Persistable
import io.requery.query.NamedNumericExpression
import io.requery.reactivex.ReactiveEntityStore
import java.math.BigDecimal

/**
 * Non-optimized calculator, but without loss of precision.
 */
class NonOptimizedAccountBalanceCalculator(
    data: ReactiveEntityStore<Persistable>,
    account: AccountView
) : AccountBalanceCalculator(data, account) {

    override fun calculateBalance(showBalanceConsumer: Consumer<BigDecimal>) {
        val accountId = account.id
        val currencyId = account.currencyId ?: error("Account $accountId hasn't currency")

        val nativeIncomes =
            calculateSumInNativeCurrency(accountId, currencyId, OperationType.incomeTypes)
        val nativeExpenses =
            calculateSumInNativeCurrency(accountId, currencyId, OperationType.expenseTypes)

        var balance = (account.initialBalance ?: BigDecimal.ZERO)
            .add(nativeIncomes)
            .subtract(nativeExpenses)

        if (accountHasOperationsInForeignCurrency(accountId, currencyId)) {
            val foreignIncomes = calculateSumInForeignCurrency(
                accountId, currencyId,
                OperationType.incomeTypes
            )
            val foreignExpenses = calculateSumInForeignCurrency(
                accountId, currencyId,
                OperationType.expenseTypes
            )
            balance = balance
                .add(foreignIncomes)
                .subtract(foreignExpenses)
        }

        showBalanceConsumer.accept(balance)
    }

    private fun accountHasOperationsInForeignCurrency(
        accountId: Int,
        nativeCurrencyId: Int
    ): Boolean {
        val exchangeRatesNotInNativeCurrency = data
            .select(ExchangeRate::class.java)
            .join(Operation::class.java)
            .on(ExchangeRate.ID.eq(Operation.EXCHANGE_RATE_ID))
            .where(
                ExchangeRate.CURRENCY_ID.notEqual(nativeCurrencyId)
                    .and(Operation.ACCOUNT_ID.equal(accountId))
            )
            .get().toList()
        return exchangeRatesNotInNativeCurrency.isNotEmpty()
    }

    private fun calculateSumInNativeCurrency(
        accountId: Int,
        currencyId: Int,
        types: List<OperationType>
    ): BigDecimal {
        val sumInNativeCurrency: Long? = data
            .select(
                NamedNumericExpression.ofLong("t_native.value").sum()
                    .`as`("native_sum")
            )
            .from(
                data
                    .select(Operation.VALUE.`as`("value"))
                    .join(ExchangeRate::class.java)
                    .on(ExchangeRate.CURRENCY_ID.eq(currencyId))
                    .and(ExchangeRate.ID.eq(Operation.EXCHANGE_RATE_ID))
                    .where(
                        Operation.TYPE.`in`(types)
                            .and(Operation.ACCOUNT_ID.eq(accountId))
                    )
                    .`as`("t_native")
            )
            .get().first().get("native_sum")

        return sumInNativeCurrency
            ?.let { BigDecimalConverterUtils.worthToBigDecimal(sumInNativeCurrency) }
            ?: BigDecimal.ZERO
    }

    // TODO rewrite this method after the issue https://github.com/requery/requery/issues/593 is closed
    // TODO before change the calculation depending on IExchangeRate.getRelatedToCurrency() - ICurrency
    private fun calculateSumInForeignCurrency(
        accountId: Int,
        currencyId: Int,
        types: List<OperationType>
    ): BigDecimal {
        val exchangeRates = data
            .select(ExchangeRate::class.java)
            .join(Operation::class.java)
            .on(ExchangeRate.ID.eq(Operation.EXCHANGE_RATE_ID))
            .where(
                ExchangeRate.CURRENCY_ID.notEqual(currencyId)
                    .and(
                        Operation.ACCOUNT_ID.equal(accountId)
                            .and(Operation.TYPE.`in`(types))
                    )
            )
            .get().toList()

        val sumOfOperationsByExchangeRates =
            calculateSumByExchangeRates(accountId, types, exchangeRates)

        return calculateTotalProduct(sumOfOperationsByExchangeRates)
    }

    private fun calculateSumByExchangeRates(
        accountId: Int,
        types: List<OperationType>,
        exchangeRates: List<ExchangeRate>
    ): Map<ExchangeRate, BigDecimal> {
        return exchangeRates.associateWith { exchangeRate ->
            calculateSumByExchangeRate(accountId, types, exchangeRate)
        }
    }

    private fun calculateSumByExchangeRate(
        accountId: Int,
        types: List<OperationType>,
        exchangeRate: ExchangeRate
    ): BigDecimal {
        val sumInForeignCurrency: Long? = data
            .select(
                NamedNumericExpression.ofLong("t_foreign.value").sum()
                    .`as`("foreign_sum")
            )
            .from(
                data
                    .select(Operation.VALUE.`as`("value"))
                    .where(
                        Operation.TYPE.`in`(types)
                            .and(Operation.ACCOUNT_ID.eq(accountId))
                            .and(Operation.EXCHANGE_RATE_ID.eq(exchangeRate.id))
                    )
                    .`as`("t_foreign")
            )
            .get().first().get("foreign_sum")

        return sumInForeignCurrency
            ?.let { BigDecimalConverterUtils.worthToBigDecimal(sumInForeignCurrency) }
            ?: BigDecimal.ZERO
    }

    private fun calculateTotalProduct(
        sumOfOperationsByExchangeRates: Map<ExchangeRate, BigDecimal>
    ): BigDecimal {
        return sumOfOperationsByExchangeRates
            .map { calculateProductOfExchangeRateAndSum(it.key, it.value) }
            .fold(BigDecimal.ZERO, { result, value -> result.add(value) })
    }

    private fun calculateProductOfExchangeRateAndSum(
        exchangeRate: ExchangeRate,
        sumOfOperations: BigDecimal
    ): BigDecimal {
        return exchangeRate.value.multiply(sumOfOperations)
    }
}
