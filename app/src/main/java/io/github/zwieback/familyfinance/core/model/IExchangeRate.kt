package io.github.zwieback.familyfinance.core.model

import androidx.databinding.Bindable
import io.github.zwieback.familyfinance.core.model.constant.Tables
import io.github.zwieback.familyfinance.core.model.converter.BigDecimalToExchangeRateConverter
import io.github.zwieback.familyfinance.core.model.converter.LocalDateConverter
import io.requery.*
import org.threeten.bp.LocalDate
import java.math.BigDecimal

@Entity(propertyNameStyle = PropertyNameStyle.FLUENT_BEAN)
@Table(name = Tables.EXCHANGE_RATE)
interface IExchangeRate : IBaseEntity {

    @get:Bindable
    @get:ManyToOne(cascade = [CascadeAction.NONE])
    @get:Column(name = "currency_id", nullable = false)
    val currency: ICurrency

    @get:Bindable
    @get:Column(name = "_value", nullable = false)
    @get:Convert(BigDecimalToExchangeRateConverter::class)
    val value: BigDecimal

    @get:Bindable
    @get:Column(name = "_date", nullable = false)
    @get:Convert(LocalDateConverter::class)
    val date: LocalDate
}
