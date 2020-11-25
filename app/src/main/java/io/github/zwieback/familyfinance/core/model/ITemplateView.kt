package io.github.zwieback.familyfinance.core.model

import androidx.databinding.Bindable
import io.github.zwieback.familyfinance.core.model.constant.*
import io.github.zwieback.familyfinance.core.model.converter.BigDecimalToExchangeRateConverter
import io.github.zwieback.familyfinance.core.model.converter.BigDecimalToWorthConverter
import io.github.zwieback.familyfinance.core.model.converter.LocalDateConverter
import io.github.zwieback.familyfinance.core.model.type.TemplateType
import io.requery.*
import org.threeten.bp.LocalDate
import java.math.BigDecimal

@Entity(propertyNameStyle = PropertyNameStyle.FLUENT_BEAN)
@View(name = Views.TEMPLATE)
interface ITemplateView : IBaseEntity {

    @get:Nullable
    @get:Column(name = "account_id")
    val accountId: Int?

    @get:Bindable
    @get:Nullable
    @get:Column(name = "account_name", length = AccountRestriction.NAME_MAX_LENGTH)
    val accountName: String?

    @get:Nullable
    @get:Column(name = "transfer_account_id")
    val transferAccountId: Int?

    @get:Bindable
    @get:Nullable
    @get:Column(name = "transfer_account_name", length = AccountRestriction.NAME_MAX_LENGTH)
    val transferAccountName: String?

    @get:Nullable
    @get:Column(name = "article_id")
    val articleId: Int?

    @get:Bindable
    @get:Nullable
    @get:Column(name = "article_name", length = ArticleRestriction.NAME_MAX_LENGTH)
    val articleName: String?

    @get:Nullable
    @get:Column(name = "article_parent_id")
    val articleParentId: Int?

    @get:Bindable
    @get:Nullable
    @get:Column(name = "article_parent_name", length = ArticleRestriction.NAME_MAX_LENGTH)
    val articleParentName: String?

    @get:Nullable
    @get:Column(name = "owner_id")
    val ownerId: Int?

    @get:Bindable
    @get:Nullable
    @get:Column(name = "owner_name", length = PersonRestriction.NAME_MAX_LENGTH)
    val ownerName: String?

    @get:Nullable
    @get:Column(name = "to_whom_id")
    val toWhomId: Int?

    @get:Bindable
    @get:Nullable
    @get:Column(name = "to_whom_name", length = PersonRestriction.NAME_MAX_LENGTH)
    val toWhomName: String?

    @get:Nullable
    @get:Column(name = "exchange_rate_id")
    val exchangeRateId: Int?

    @get:Bindable
    @get:Nullable
    @get:Column(name = "exchange_rate_value")
    @get:Convert(BigDecimalToExchangeRateConverter::class)
    val exchangeRateValue: BigDecimal?

    @get:Nullable
    @get:Column(name = "currency_id")
    val currencyId: Int?

    @get:Bindable
    @get:Nullable
    @get:Column(name = "currency_name", length = CurrencyRestriction.NAME_MAX_LENGTH)
    val currencyName: String?

    @get:Bindable
    @get:Column(nullable = false, length = TemplateRestriction.NAME_MAX_LENGTH)
    val name: String

    @get:Column(name = "_type", nullable = false, length = TemplateRestriction.TYPE_MAX_LENGTH)
    val type: TemplateType

    @get:Bindable
    @get:Nullable
    @get:Column(name = "_date")
    @get:Convert(LocalDateConverter::class)
    val date: LocalDate?

    @get:Bindable
    @get:Nullable
    @get:Column(name = "_value")
    @get:Convert(BigDecimalToWorthConverter::class)
    val value: BigDecimal?

    @get:Bindable
    @get:Nullable
    @get:Column(length = OperationRestriction.DESCRIPTION_MAX_LENGTH)
    val description: String?

    @get:Bindable
    @get:Nullable
    @get:Column(length = OperationRestriction.URL_MAX_LENGTH)
    val url: String?
}
