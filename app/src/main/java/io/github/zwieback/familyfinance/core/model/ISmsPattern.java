package io.github.zwieback.familyfinance.core.model;

import android.databinding.Bindable;

import io.github.zwieback.familyfinance.core.model.restriction.SmsPatternRestriction;
import io.requery.CascadeAction;
import io.requery.Column;
import io.requery.Entity;
import io.requery.ManyToOne;
import io.requery.Nullable;
import io.requery.PropertyNameStyle;
import io.requery.Table;

@Entity(propertyNameStyle = PropertyNameStyle.FLUENT_BEAN)
@Table(name = "t_sms_pattern")
public interface ISmsPattern extends IBaseEntity {

    @Bindable
    @ManyToOne(cascade = CascadeAction.NONE)
    @Column(name = "template_id")
    ITemplate getTemplate();

    @Bindable
    @Column(nullable = false, length = SmsPatternRestriction.NAME_MAX_LENGTH)
    String getName();

    @Bindable
    @Column(nullable = false, length = SmsPatternRestriction.REGEX_MAX_LENGTH)
    String getRegex();

    @Bindable
    @Column(nullable = false, length = SmsPatternRestriction.SENDER_MAX_LENGTH)
    String getSender();

    @Bindable
    @Nullable
    @Column(name = "date_group")
    Integer getDateGroup();

    @Bindable
    @Nullable
    @Column(name = "value_group")
    Integer getValueGroup();
}
