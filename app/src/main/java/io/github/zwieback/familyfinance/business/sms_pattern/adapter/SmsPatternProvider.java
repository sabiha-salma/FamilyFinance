package io.github.zwieback.familyfinance.business.sms_pattern.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.core.adapter.EntityProvider;
import io.github.zwieback.familyfinance.core.model.SmsPattern;

public class SmsPatternProvider extends EntityProvider<SmsPattern> {

    public SmsPatternProvider(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public IIcon provideDefaultIcon(SmsPattern smsPattern) {
        if (smsPattern.isCommon()) {
            return FontAwesome.Icon.faw_comment;
        }
        return FontAwesome.Icon.faw_comment1;
    }

    @Override
    public int provideDefaultIconColor(SmsPattern smsPattern) {
        return R.color.colorPrimaryDark;
    }

    @Override
    public int provideTextColor(SmsPattern smsPattern) {
        return ContextCompat.getColor(context, provideDefaultIconColor(smsPattern));
    }
}
