package io.github.zwieback.familyfinance.core.model

import androidx.databinding.Bindable
import io.github.zwieback.familyfinance.business.sms.handler.SmsHandler
import io.github.zwieback.familyfinance.core.model.restriction.SmsPatternRestriction
import io.requery.*

@Entity(propertyNameStyle = PropertyNameStyle.FLUENT_BEAN, copyable = true)
@Table(name = "t_sms_pattern")
interface ISmsPattern : IBaseEntity {

    @get:Bindable
    @get:ManyToOne(cascade = [CascadeAction.NONE])
    @get:Column(name = "template_id", nullable = false)
    val template: ITemplate

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

    /**
     * Required to create a notification about a partially completed operation
     * in the [SmsHandler]
     */
    @get:Bindable
    @get:Column(nullable = false, value = "0")
    val isCommon: Boolean
}
