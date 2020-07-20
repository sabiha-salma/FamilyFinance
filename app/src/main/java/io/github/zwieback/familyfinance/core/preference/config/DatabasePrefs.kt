package io.github.zwieback.familyfinance.core.preference.config

import android.content.Context
import com.afollestad.rxkprefs.Pref
import com.afollestad.rxkprefs.RxkPrefs
import com.afollestad.rxkprefs.rxkPrefs
import io.github.zwieback.familyfinance.constant.IdConstants.EMPTY_ID

class DatabasePrefs private constructor(context: Context) {

    private val prefs: RxkPrefs = rxkPrefs(context, FILE_NAME)

    private val currencyIdPref: Pref<Int>
        get() = prefs.integer(CURRENCY_ID, DEFAULT_VALUE)
    private val accountIdPref: Pref<Int>
        get() = prefs.integer(ACCOUNT_ID, DEFAULT_VALUE)
    private val personIdPref: Pref<Int>
        get() = prefs.integer(PERSON_ID, DEFAULT_VALUE)

    private val incomesArticleIdPref: Pref<Int>
        get() = prefs.integer(INCOMES_ARTICLE_ID, DEFAULT_VALUE)
    private val expensesArticleIdPref: Pref<Int>
        get() = prefs.integer(EXPENSES_ARTICLE_ID, DEFAULT_VALUE)
    private val transferArticleIdPref: Pref<Int>
        get() = prefs.integer(TRANSFER_ARTICLE_ID, DEFAULT_VALUE)

    var currencyId
        get() = currencyIdPref.get()
        set(value) = currencyIdPref.set(value)
    var accountId
        get() = accountIdPref.get()
        set(value) = accountIdPref.set(value)
    var personId
        get() = personIdPref.get()
        set(value) = personIdPref.set(value)

    var incomesArticleId
        get() = incomesArticleIdPref.get()
        set(value) = incomesArticleIdPref.set(value)
    var expensesArticleId
        get() = expensesArticleIdPref.get()
        set(value) = expensesArticleIdPref.set(value)
    var transferArticleId
        get() = transferArticleIdPref.get()
        set(value) = transferArticleIdPref.set(value)

    companion object {
        const val FILE_NAME = "database_prefs"

        // region preferences
        private const val CURRENCY_ID = "currencyId"
        private const val ACCOUNT_ID = "accountId"
        private const val PERSON_ID = "personId"

        private const val INCOMES_ARTICLE_ID = "incomesArticleId"
        private const val EXPENSES_ARTICLE_ID = "expensesArticleId"
        private const val TRANSFER_ARTICLE_ID = "transferArticleId"

        private const val DEFAULT_VALUE = EMPTY_ID
        // endregion preferences

        fun with(context: Context) = DatabasePrefs(context)
    }
}
