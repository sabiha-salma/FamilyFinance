package io.github.zwieback.familyfinance.core.model

import androidx.databinding.Bindable
import io.github.zwieback.familyfinance.core.model.converter.BigDecimalToWorthConverter
import io.github.zwieback.familyfinance.core.model.restriction.AccountRestriction
import io.github.zwieback.familyfinance.core.model.restriction.CurrencyRestriction
import io.github.zwieback.familyfinance.core.model.restriction.PersonRestriction
import io.github.zwieback.familyfinance.core.model.type.AccountType
import io.requery.*
import java.math.BigDecimal

@Entity(propertyNameStyle = PropertyNameStyle.FLUENT_BEAN)
@View(name = "v_account")
interface IAccountView : IBaseEntityFolder {

    @get:Column(nullable = false, value = "true")
    val isActive: Boolean

    @get:Nullable
    @get:Column(name = "parent_id")
    val parentId: Int?

    @get:Bindable
    @get:Nullable
    @get:Column(name = "parent_name", length = AccountRestriction.NAME_MAX_LENGTH)
    val parentName: String?

    /**
     * May be `null` only if [isFolder]
     */
    @get:Nullable
    @get:Column(name = "currency_id")
    val currencyId: Int?

    /**
     * May be `null` only if [isFolder]
     */
    @get:Bindable
    @get:Nullable
    @get:Column(name = "currency_name", length = CurrencyRestriction.NAME_MAX_LENGTH)
    val currencyName: String?

    /**
     * May be `null` only if [isFolder]
     */
    @get:Nullable
    @get:Column(name = "owner_id")
    val ownerId: Int?

    /**
     * May be `null` only if [isFolder]
     */
    @get:Bindable
    @get:Nullable
    @get:Column(name = "owner_name", length = PersonRestriction.NAME_MAX_LENGTH)
    val ownerName: String?

    @get:Bindable
    @get:Column(nullable = false, length = AccountRestriction.NAME_MAX_LENGTH)
    val name: String

    /**
     * May be `null` only if [isFolder]
     */
    @get:Bindable
    @get:Nullable
    @get:Column(name = "initial_balance")
    @get:Convert(BigDecimalToWorthConverter::class)
    val initialBalance: BigDecimal?

    @get:Bindable
    @get:Column(name = "order_code", nullable = false)
    val orderCode: Int

    @get:Column(
        name = "_type",
        nullable = false,
        length = AccountRestriction.TYPE_MAX_LENGTH,
        value = "UNDEFINED_ACCOUNT"
    )
    val type: AccountType

    @get:Bindable
    @get:Nullable
    @get:Column(name = "_number", length = AccountRestriction.NUMBER_MAX_LENGTH)
    val number: String?

    @get:Bindable
    @get:Nullable
    @get:Column(name = "payment_system", length = AccountRestriction.PAYMENT_SYSTEM_MAX_LENGTH)
    val paymentSystem: String?

    /**
     * May be `null` only if [AccountType.isCard] is `false`
     */
    @get:Bindable
    @get:Nullable
    @get:Column(name = "card_number", length = AccountRestriction.CARD_NUMBER_MAX_LENGTH)
    val cardNumber: String?
}
