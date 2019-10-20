package io.github.zwieback.familyfinance.core.model.type

/**
 * After changing this enum, you also need to change the `strings/account_type`
 * array too.
 */
enum class AccountType {
    UNDEFINED_ACCOUNT,
    CASH_ACCOUNT,
    DEPOSIT_ACCOUNT,
    CREDIT_ACCOUNT,
    DEBIT_CARD_ACCOUNT,
    CREDIT_CARD_ACCOUNT,
    VIRTUAL_DEBIT_CARD_ACCOUNT,
    VIRTUAL_CREDIT_CARD_ACCOUNT;

    val isCard: Boolean
        get() = CARD_LIST.contains(this)

    companion object {

        private val CARD_LIST = listOf(
            DEBIT_CARD_ACCOUNT,
            CREDIT_CARD_ACCOUNT,
            VIRTUAL_DEBIT_CARD_ACCOUNT,
            VIRTUAL_CREDIT_CARD_ACCOUNT
        )
    }
}
