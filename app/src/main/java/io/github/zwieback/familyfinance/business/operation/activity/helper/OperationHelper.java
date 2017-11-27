package io.github.zwieback.familyfinance.business.operation.activity.helper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import io.github.zwieback.familyfinance.business.operation.lifecycle.destroyer.OperationForceDestroyer;
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer;
import io.github.zwieback.familyfinance.core.model.Operation;
import io.github.zwieback.familyfinance.core.model.OperationView;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public abstract class OperationHelper {

    final Context context;
    final ReactiveEntityStore<Persistable> data;

    OperationHelper(Context context, ReactiveEntityStore<Persistable> data) {
        this.context = context;
        this.data = data;
    }

    public abstract Intent getIntentToAdd(@Nullable Integer accountId);

    public abstract Intent getIntentToEdit(OperationView operation);

    public abstract Intent getIntentToDuplicate(OperationView operation);

    public EntityDestroyer<Operation> createDestroyer(OperationView operation) {
        return new OperationForceDestroyer(context, data);
    }
}
