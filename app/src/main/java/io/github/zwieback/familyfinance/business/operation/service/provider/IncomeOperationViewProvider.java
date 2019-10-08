package io.github.zwieback.familyfinance.business.operation.service.provider;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.core.adapter.EntityProvider;
import io.github.zwieback.familyfinance.core.model.OperationView;

public class IncomeOperationViewProvider extends EntityProvider<OperationView> {

    public IncomeOperationViewProvider(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public IIcon provideDefaultIcon(OperationView operation) {
        return FontAwesome.Icon.faw_plus_circle;
    }

    @Override
    public int provideDefaultIconColor(OperationView operation) {
        return R.color.colorIncome;
    }

    @Override
    public int provideTextColor(OperationView operation) {
        return ContextCompat.getColor(context, provideDefaultIconColor(operation));
    }
}
