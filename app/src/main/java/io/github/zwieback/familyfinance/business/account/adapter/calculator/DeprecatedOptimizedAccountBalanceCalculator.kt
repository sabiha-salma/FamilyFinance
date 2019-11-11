package io.github.zwieback.familyfinance.business.account.adapter.calculator

import io.github.zwieback.familyfinance.core.model.AccountView
import io.github.zwieback.familyfinance.core.model.type.OperationType
import io.github.zwieback.familyfinance.util.BigDecimalConverterUtils
import io.reactivex.functions.Consumer
import io.requery.Persistable
import io.requery.query.Tuple
import io.requery.reactivex.ReactiveEntityStore
import java.math.BigDecimal

/**
 * The optimized calculator, but with loss of precision.
 */
@Deprecated(
    "This calculator's result has loss of precision",
    ReplaceWith("Use NonOptimizedAccountBalanceCalculator instead")
)
class DeprecatedOptimizedAccountBalanceCalculator(
    data: ReactiveEntityStore<Persistable>,
    account: AccountView
) : AccountBalanceCalculator(data, account) {

    override fun calculateBalance(showBalanceConsumer: Consumer<BigDecimal>) {
        val accountId = account.id
        val currencyId = account.currencyId
        val query = "select" +
                "  current_account.initial_balance + (incomes.sum_value - expenses.sum_value)" +
                "    as 'balance_in_native_currency'," +
                "  cast(foreign_incomes.sum_value - foreign_expenses.sum_value as float)" +
                "    as 'balance_in_foreign_currency'" +
                " from" +
                "  (" +
                "    select coalesce(sum(incomes._value), 0) as sum_value" +
                "      from operation as incomes" +
                "           inner join exchange_rate er on incomes.exchange_rate_id = er.id" +
                "     where" +
                "           incomes._type in ?" +
                "       and incomes.account_id = $accountId" +
                "       and er.currency_id = $currencyId" +
                "  ) as incomes," +
                "  (" +
                "    select coalesce(sum(expenses._value), 0) as sum_value" +
                "      from operation as expenses" +
                "           inner join exchange_rate er on expenses.exchange_rate_id = er.id" +
                "     where" +
                "           expenses._type in ?" +
                "       and expenses.account_id = $accountId" +
                "       and er.currency_id = $currencyId" +
                "  ) as expenses," +
                "  (" +
                "    select coalesce(acc.initial_balance, 0) as initial_balance" +
                "      from account as acc" +
                "     where acc.id = $accountId" +
                "  ) as current_account," +
                "  (" +
                "    select coalesce(sum(incomes._value * er._value), 0) as sum_value" +
                "      from operation as incomes" +
                "           inner join exchange_rate er on incomes.exchange_rate_id = er.id" +
                "     where" +
                "           incomes._type in ?" +
                "       and incomes.account_id = $accountId" +
                "       and er.currency_id <> $currencyId" +
                "  ) as foreign_incomes," +
                "  (" +
                "    select coalesce(sum(expenses._value * er._value), 0) as sum_value" +
                "      from operation as expenses" +
                "           inner join exchange_rate er on expenses.exchange_rate_id = er.id" +
                "     where" +
                "           expenses._type in ?" +
                "       and expenses.account_id = $accountId" +
                "       and er.currency_id <> $currencyId" +
                "  ) as foreign_expenses" +
                ";"
        val incomeTypes = OperationType.incomeTypes
        val expenseTypes = OperationType.expenseTypes
        val parameters = arrayOf(incomeTypes, expenseTypes, incomeTypes, expenseTypes)
        val result = data.raw(query, parameters)

        result.observable().subscribe { tuple ->
            val balanceInNativeCurrency = extractBalance(
                tuple,
                "balance_in_native_currency"
            ) { BigDecimalConverterUtils.balanceInNativeCurrencyToBigDecimal(it) }
            val balanceInForeignCurrency = extractBalance(
                tuple,
                "balance_in_foreign_currency"
            ) { BigDecimalConverterUtils.balanceInForeignCurrencyToBigDecimal(it) }
            val balance = balanceInNativeCurrency.add(balanceInForeignCurrency)

            showBalanceConsumer.accept(balance)
        }
    }

    private fun extractBalance(
        tuple: Tuple,
        key: String,
        balanceFunction: (Long) -> BigDecimal?
    ): BigDecimal {
        val preliminaryBalance: Long? = tuple.get(key)
        return preliminaryBalance
            ?.let { balanceFunction(preliminaryBalance) }
            ?: BigDecimal.ZERO
    }
}
