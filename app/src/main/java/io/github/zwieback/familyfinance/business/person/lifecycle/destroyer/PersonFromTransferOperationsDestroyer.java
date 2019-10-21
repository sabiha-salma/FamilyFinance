package io.github.zwieback.familyfinance.business.person.lifecycle.destroyer;

import android.content.Context;

import io.github.zwieback.familyfinance.R;
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityDestroyer;
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityFromDestroyer;
import io.github.zwieback.familyfinance.core.model.Operation;
import io.github.zwieback.familyfinance.core.model.Person;
import io.requery.Persistable;
import io.requery.query.Condition;
import io.requery.reactivex.ReactiveEntityStore;

import static io.github.zwieback.familyfinance.core.model.type.OperationType.TRANSFER_EXPENSE_OPERATION;
import static io.github.zwieback.familyfinance.core.model.type.OperationType.TRANSFER_INCOME_OPERATION;

class PersonFromTransferOperationsDestroyer extends EntityFromDestroyer<Person, Operation> {

    PersonFromTransferOperationsDestroyer(Context context, ReactiveEntityStore<Persistable> data) {
        super(context, data);
    }

    @Override
    protected EntityDestroyer<Person> next() {
        return new PersonFromPreferencesDestroyer(getContext(), getData());
    }

    @Override
    protected Class<Operation> getFromClass() {
        return Operation.class;
    }

    @Override
    protected Condition<?, ?> getWhereCondition(Person person) {
        return Operation.TYPE.in(TRANSFER_EXPENSE_OPERATION, TRANSFER_INCOME_OPERATION)
                .and(Operation.OWNER_ID.eq(person.getId()));
    }

    @Override
    protected int getAlertResourceId() {
        return R.string.transfer_operations_with_owner_exists;
    }
}
