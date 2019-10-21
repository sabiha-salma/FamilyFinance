package io.github.zwieback.familyfinance.business.sms_pattern.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.core.adapter.EntityProvider;
import io.github.zwieback.familyfinance.core.model.SmsPatternView;

class SmsPatternViewProvider extends EntityProvider<SmsPatternView> {

    SmsPatternViewProvider(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public IIcon provideDefaultIcon(SmsPatternView smsPattern) {
        if (smsPattern.isCommon()) {
            return FontAwesome.Icon.faw_comment;
        }
        return FontAwesome.Icon.faw_comment1;
    }

    @Override
    public int provideDefaultIconColor(SmsPatternView smsPattern) {
        return R.color.colorPrimaryDark;
    }

    @Override
    public int provideTextColor(SmsPatternView smsPattern) {
        return ContextCompat.getColor(getContext(), provideDefaultIconColor(smsPattern));
    }
}
