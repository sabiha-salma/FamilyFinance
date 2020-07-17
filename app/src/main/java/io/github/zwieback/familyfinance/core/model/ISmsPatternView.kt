package io.github.zwieback.familyfinance.core.model

import androidx.databinding.Bindable
import io.github.zwieback.familyfinance.core.model.constant.SmsPatternRestriction
import io.github.zwieback.familyfinance.core.model.constant.TemplateRestriction
import io.github.zwieback.familyfinance.core.model.constant.Views
import io.requery.*

@Entity(propertyNameStyle = PropertyNameStyle.FLUENT_BEAN)
@View(name = Views.SMS_PATTERN)
interface ISmsPatternView : IBaseEntity {

    @get:Column(name = "template_id")
    val templateId: Int

    @get:Bindable
    @get:Column(
        nullable = false,
        name = "template_name",
        length = TemplateRestriction.NAME_MAX_LENGTH
    )
    val templateName: String

    @get:Bindable
    @get:Column(nullable = false, length = SmsPatternRestriction.NAME_MAX_LENGTH)
    val name: String

    @get:Bindable
    @get:Column(nullable = false, length = SmsPatternRestriction.REGEX_MAX_LENGTH)
    val regex: String

    @get:Bindable
    @get:Column(nullable = false, length = SmsPatternRestriction.SENDER_MAX_LENGTH)
    val sender: String

    @get:Bindable
    @get:Nullable
    @get:Column(name = "date_group")
    val dateGroup: Int?

    @get:Bindable
    @get:Nullable
    @get:Column(name = "value_group")
    val valueGroup: Int?

    @get:Bindable
    @get:Column(nullable = false, value = "false")
    val isCommon: Boolean
}
