package io.github.zwieback.familyfinance.core.model

import androidx.databinding.Bindable
import io.github.zwieback.familyfinance.core.model.restriction.CurrencyRestriction
import io.requery.Column
import io.requery.Entity
import io.requery.PropertyNameStyle
import io.requery.Table

@Entity(propertyNameStyle = PropertyNameStyle.FLUENT_BEAN)
@Table(name = "currency")
interface ICurrency : IBaseEntity {

    @get:Bindable
    @get:Column(nullable = false, length = CurrencyRestriction.NAME_MAX_LENGTH)
    val name: String

    @get:Bindable
    @get:Column(nullable = false, length = CurrencyRestriction.DESCRIPTION_MAX_LENGTH)
    val description: String
}
