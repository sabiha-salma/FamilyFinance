package io.github.zwieback.familyfinance.core.model

import androidx.databinding.Bindable
import io.github.zwieback.familyfinance.core.model.converter.BigDecimalToWorthConverter
import io.github.zwieback.familyfinance.core.model.restriction.AccountRestriction
import io.github.zwieback.familyfinance.core.model.type.AccountType
import io.requery.*
import java.math.BigDecimal

@Entity(propertyNameStyle = PropertyNameStyle.FLUENT_BEAN, copyable = true)
@Table(name = "account")
interface IAccount : IBaseEntityFolder {

    @get:Column(nullable = false, value = "true")
    val isActive: Boolean

    @get:Bindable
    @get:Nullable
    @get:ForeignKey
    @get:OneToOne(mappedBy = "id", cascade = [CascadeAction.NONE])
    @get:Index("idx_parent_account_id")
    @get:Column(name = "parent_id")
    val parent: IAccount?

    /**
     * May be `null` only if [isFolder]
     */
    @get:Bindable
    @get:Nullable
    @get:ManyToOne(cascade = [CascadeAction.NONE])
    @get:Column(name = "currency_id")
    val currency: ICurrency?

    /**
     * May be `null` only if [isFolder]
     */
    @get:Bindable
    @get:Nullable
    @get:ManyToOne(cascade = [CascadeAction.NONE])
    @get:Column(name = "owner_id")
    val owner: IPerson?

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
