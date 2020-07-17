package io.github.zwieback.familyfinance.core.model

import androidx.databinding.Bindable
import io.github.zwieback.familyfinance.core.model.constant.OperationRestriction
import io.github.zwieback.familyfinance.core.model.constant.Tables
import io.github.zwieback.familyfinance.core.model.converter.BigDecimalToWorthConverter
import io.github.zwieback.familyfinance.core.model.converter.LocalDateConverter
import io.github.zwieback.familyfinance.core.model.type.OperationType
import io.requery.*
import org.threeten.bp.LocalDate
import java.math.BigDecimal

@Entity(propertyNameStyle = PropertyNameStyle.FLUENT_BEAN)
@Table(name = Tables.OPERATION)
interface IOperation : IBaseEntity {

    /**
     * Not `null` only if [type] in
     * ([OperationType.TRANSFER_EXPENSE_OPERATION],
     * [OperationType.TRANSFER_INCOME_OPERATION])
     */
    @get:ForeignKey
    @get:Nullable
    @get:OneToOne(mappedBy = "id", cascade = [CascadeAction.NONE])
    @get:Column(name = "linked_transfer_operation_id")
    val linkedTransferOperation: IOperation?

    @get:Bindable
    @get:ManyToOne(cascade = [CascadeAction.NONE])
    @get:Column(name = "account_id", nullable = false)
    val account: IAccount

    @get:Bindable
    @get:ManyToOne(cascade = [CascadeAction.NONE])
    @get:Column(name = "article_id", nullable = false)
    val article: IArticle

    @get:Bindable
    @get:ManyToOne(cascade = [CascadeAction.NONE])
    @get:Column(name = "owner_id", nullable = false)
    val owner: IPerson

    @get:Bindable
    @get:ManyToOne(cascade = [CascadeAction.NONE])
    @get:Column(name = "exchange_rate_id", nullable = false)
    val exchangeRate: IExchangeRate

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
