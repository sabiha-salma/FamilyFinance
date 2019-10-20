package io.github.zwieback.familyfinance.core.model

import androidx.databinding.Bindable
import io.github.zwieback.familyfinance.core.model.converter.BigDecimalToExchangeRateConverter
import io.github.zwieback.familyfinance.core.model.converter.BigDecimalToWorthConverter
import io.github.zwieback.familyfinance.core.model.converter.LocalDateConverter
import io.github.zwieback.familyfinance.core.model.restriction.*
import io.github.zwieback.familyfinance.core.model.type.OperationType
import io.requery.*
import org.threeten.bp.LocalDate
import java.math.BigDecimal

@Entity(propertyNameStyle = PropertyNameStyle.FLUENT_BEAN)
@View(name = "v_operation")
interface IOperationView : IBaseEntity {

    /**
     * Not `null` only if [type] in
     * ([OperationType.TRANSFER_EXPENSE_OPERATION],
     * [OperationType.TRANSFER_INCOME_OPERATION])
     */
    @get:Nullable
    @get:Column(name = "linked_transfer_operation_id")
    val linkedTransferOperationId: Int?

    @get:Column(name = "account_id", nullable = false)
    val accountId: Int

    @get:Bindable
    @get:Column(
        name = "account_name",
        nullable = false,
        length = AccountRestriction.NAME_MAX_LENGTH
    )
    val accountName: String

    @get:Column(name = "article_id", nullable = false)
    val articleId: Int

    @get:Bindable
    @get:Column(
        name = "article_name",
        nullable = false,
        length = ArticleRestriction.NAME_MAX_LENGTH
    )
    val articleName: String

    @get:Nullable
    @get:Column(name = "article_parent_id")
    val articleParentId: Int?

    @get:Bindable
    @get:Nullable
    @get:Column(name = "article_parent_name", length = ArticleRestriction.NAME_MAX_LENGTH)
    val articleParentName: String?

    @get:Column(name = "owner_id", nullable = false)
    val ownerId: Int

    @get:Bindable
    @get:Column(name = "owner_name", nullable = false, length = PersonRestriction.NAME_MAX_LENGTH)
    val ownerName: String

    @get:Column(name = "exchange_rate_id", nullable = false)
    val exchangeRateId: Int

    @get:Bindable
    @get:Column(name = "exchange_rate_value", nullable = false)
    @get:Convert(BigDecimalToExchangeRateConverter::class)
    val exchangeRateValue: BigDecimal

    @get:Column(name = "currency_id", nullable = false)
    val currencyId: Int

    @get:Bindable
    @get:Column(
        name = "currency_name",
        nullable = false,
        length = CurrencyRestriction.NAME_MAX_LENGTH
    )
    val currencyName: String

    @get:Column(name = "_type", nullable = false, length = OperationRestriction.TYPE_MAX_LENGTH)
    val type: OperationType

    @get:Bindable
    @get:Column(name = "_date", nullable = false)
    @get:Convert(LocalDateConverter::class)
    val date: LocalDate

    @get:Bindable
    @get:Column(name = "_value", nullable = false)
    @get:Convert(BigDecimalToWorthConverter::class)
    val value: BigDecimal

    @get:Bindable
    @get:Nullable
    @get:Column(length = OperationRestriction.DESCRIPTION_MAX_LENGTH)
    val description: String?

    @get:Bindable
    @get:Nullable
    @get:Column(length = OperationRestriction.URL_MAX_LENGTH)
    val url: String?
}
