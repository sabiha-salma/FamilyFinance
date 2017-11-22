package io.github.zwieback.familyfinance.business.operation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mikepenz.iconics.typeface.IIcon;

import io.github.zwieback.familyfinance.business.operation.adapter.exception.UndefinedOperationProviderException;
import io.github.zwieback.familyfinance.core.adapter.EntityProvider;
import io.github.zwieback.familyfinance.core.model.Operation;

class FlowOfFundsOperationProvider extends EntityProvider<Operation> {

    private final IncomeOperationProvider incomeProvider;
    private final ExpenseOperationProvider expenseProvider;
    private final TransferOperationProvider transferProvider;

    FlowOfFundsOperationProvider(Context context) {
        super(context);
        incomeProvider = new IncomeOperationProvider(context);
        expenseProvider = new ExpenseOperationProvider(context);
        transferProvider = new TransferOperationProvider(context);
    }

    @NonNull
    @Override
    public IIcon provideDefaultIcon(Operation operation) {
        return determineProvider(operation).provideDefaultIcon(operation);
    }

    @Override
    public int provideDefaultIconColor(Operation operation) {
        return determineProvider(operation).provideDefaultIconColor(operation);
    }

    @Override
    public int provideTextColor(Operation operation) {
        return determineProvider(operation).provideTextColor(operation);
    }

    private EntityProvider<Operation> determineProvider(Operation operation) {
        switch (operation.getType()) {
            case EXPENSE_OPERATION:
                return expenseProvider;
            case INCOME_OPERATION:
                return incomeProvider;
            case TRANSFER_EXPENSE_OPERATION:
            case TRANSFER_INCOME_OPERATION:
                return transferProvider;
        }
        throw new UndefinedOperationProviderException(operation.getType());
    }
}
