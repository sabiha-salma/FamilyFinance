package io.github.zwieback.familyfinance.core.model

import androidx.databinding.Bindable
import io.github.zwieback.familyfinance.core.model.constant.CurrencyRestriction
import io.github.zwieback.familyfinance.core.model.constant.Views
import io.requery.Column
import io.requery.Entity
import io.requery.PropertyNameStyle
import io.requery.View

@Entity(propertyNameStyle = PropertyNameStyle.FLUENT_BEAN)
@View(name = Views.CURRENCY)
interface ICurrencyView : IBaseEntity {

    @get:Bindable
    @get:Column(nullable = false, length = CurrencyRestriction.NAME_MAX_LENGTH)
    val name: String

    @get:Bindable
    @get:Column(nullable = false, length = CurrencyRestriction.DESCRIPTION_MAX_LENGTH)
    val description: String
}
