package io.github.zwieback.familyfinance.business.account.activity.helper

import androidx.appcompat.widget.AppCompatSpinner
import io.github.zwieback.familyfinance.core.model.Account
import io.github.zwieback.familyfinance.core.model.type.AccountType

object AccountTypeHelper {

    fun getAccountTypeIndex(account: Account): Int {
        return account.type.ordinal
    }

    fun getAccountType(typeSpinner: AppCompatSpinner): AccountType {
        val typeIndex = typeSpinner.selectedItemPosition
        return AccountType.values()[typeIndex]
    }
}
