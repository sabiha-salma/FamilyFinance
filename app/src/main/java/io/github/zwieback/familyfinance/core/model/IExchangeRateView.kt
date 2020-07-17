package io.github.zwieback.familyfinance.core.model

import androidx.databinding.Bindable
import io.github.zwieback.familyfinance.core.model.constant.CurrencyRestriction
import io.github.zwieback.familyfinance.core.model.constant.Views
import io.github.zwieback.familyfinance.core.model.converter.BigDecimalToExchangeRateConverter
import io.github.zwieback.familyfinance.core.model.converter.LocalDateConverter
import io.requery.*
import org.threeten.bp.LocalDate
import java.math.BigDecimal

@Entity(propertyNameStyle = PropertyNameStyle.FLUENT_BEAN)
@View(name = Views.EXCHANGE_RATE)
interface IExchangeRateView : IBaseEntity {

    @get:Bindable
    @get:Column(name = "_value", nullable = false)
    @get:Convert(BigDecimalToExchangeRateConverter::class)
    val value: BigDecimal

    @get:Bindable
    @get:Column(name = "_date", nullable = false)
    @get:Convert(LocalDateConverter::class)
    val date: LocalDate

    @get:Column(name = "currency_id", nullable = false)
    val currencyId: Int

    @get:Bindable
    @get:Column(
        name = "currency_name",
        nullable = false,
        length = CurrencyRestriction.NAME_MAX_LENGTH
    )
    val currencyName: String
}
