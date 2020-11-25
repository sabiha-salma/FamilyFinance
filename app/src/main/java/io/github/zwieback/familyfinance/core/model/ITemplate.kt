package io.github.zwieback.familyfinance.core.model

import androidx.databinding.Bindable
import io.github.zwieback.familyfinance.core.model.constant.OperationRestriction
import io.github.zwieback.familyfinance.core.model.constant.Tables
import io.github.zwieback.familyfinance.core.model.constant.TemplateRestriction
import io.github.zwieback.familyfinance.core.model.converter.BigDecimalToWorthConverter
import io.github.zwieback.familyfinance.core.model.converter.LocalDateConverter
import io.github.zwieback.familyfinance.core.model.type.TemplateType
import io.requery.*
import org.threeten.bp.LocalDate
import java.math.BigDecimal

@Entity(propertyNameStyle = PropertyNameStyle.FLUENT_BEAN)
@Table(name = Tables.TEMPLATE)
interface ITemplate : IBaseEntity {

    @get:Bindable
    @get:Nullable
    @get:ManyToOne(cascade = [CascadeAction.NONE])
    @get:Column(name = "account_id")
    val account: IAccount?

    @get:Bindable
    @get:Nullable
    @get:ManyToOne(cascade = [CascadeAction.NONE])
    @get:Column(name = "transfer_account_id")
    val transferAccount: IAccount?

    @get:Bindable
    @get:Nullable
    @get:ManyToOne(cascade = [CascadeAction.NONE])
    @get:Column(name = "article_id")
    val article: IArticle?

    @get:Bindable
    @get:Nullable
    @get:ManyToOne(cascade = [CascadeAction.NONE])
    @get:Column(name = "owner_id")
    val owner: IPerson?

    @get:Bindable
    @get:Nullable
    @get:ManyToOne(cascade = [CascadeAction.NONE])
    @get:Column(name = "to_whom_id")
    val toWhom: IPerson?

    @get:Bindable
    @get:Nullable
    @get:ManyToOne(cascade = [CascadeAction.NONE])
    @get:Column(name = "exchange_rate_id")
    val exchangeRate: IExchangeRate?

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
