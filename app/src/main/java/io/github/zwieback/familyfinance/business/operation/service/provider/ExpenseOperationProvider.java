package io.github.zwieback.familyfinance.business.operation.service.provider;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.core.adapter.EntityProvider;
import io.github.zwieback.familyfinance.core.model.Operation;

public class ExpenseOperationProvider extends EntityProvider<Operation> {

    public ExpenseOperationProvider(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public IIcon provideDefaultIcon(Operation operation) {
        return FontAwesome.Icon.faw_minus_circle;
    }

    @Override
    public int provideDefaultIconColor(Operation operation) {
        return R.color.colorExpense;
    }

    @Override
    public int provideTextColor(Operation operation) {
        return ContextCompat.getColor(getContext(), provideDefaultIconColor(operation));
    }
}
