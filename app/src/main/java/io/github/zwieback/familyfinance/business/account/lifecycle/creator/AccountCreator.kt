package io.github.zwieback.familyfinance.business.account.lifecycle.creator

import android.content.Context
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.lifecycle.creator.EntityCreator
import io.github.zwieback.familyfinance.core.model.Account
import io.github.zwieback.familyfinance.core.model.Currency
import io.github.zwieback.familyfinance.core.model.Person
import io.github.zwieback.familyfinance.core.model.type.AccountType
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore
import java.math.BigDecimal

class AccountCreator(
    context: Context,
    data: ReactiveEntityStore<Persistable>
) : EntityCreator<Account>(context, data) {

    override fun buildEntities(): Iterable<Account> {
        val defaultCurrency = findCurrency(databasePrefs.currencyId)
        val chief = findPerson(databasePrefs.personId)
        val cash = createAccount(
            null,
            defaultCurrency,
            chief,
            getString(R.string.account_cash),
            false,
            BigDecimal.ZERO,
            1,
            AccountType.CASH_ACCOUNT
        )
        return sortedSetOf(this, cash)
    }

    override fun compare(left: Account, right: Account): Int {
        return left.orderCode.compareTo(right.orderCode)
    }

    private fun findPerson(personId: Int): Person {
        val persons = data
            .select(Person::class.java)
            .where(Person.ID.eq(personId))
            .get()
        return persons.first()
    }

    private fun findCurrency(currencyId: Int): Currency {
        val currencies = data
            .select(Currency::class.java)
            .where(Currency.ID.eq(currencyId))
            .get()
        return currencies.first()
    }

    private fun createAccount(
        parent: Account?,
        currency: Currency?,
        owner: Person?,
        name: String,
        folder: Boolean,
        initialBalance: BigDecimal?,
        orderCode: Int,
        type: AccountType
    ): Account {
        return Account()
            .setActive(true)
            .setParent(parent)
            .setCurrency(currency)
            .setOwner(owner)
            .setName(name)
            .setFolder(folder)
            .setInitialBalance(initialBalance)
            .setOrderCode(orderCode)
            .setType(type)
    }
}
