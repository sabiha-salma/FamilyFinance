package io.github.zwieback.familyfinance.core.lifecycle.creator;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.Comparator;
import java.util.concurrent.Callable;

import io.github.zwieback.familyfinance.core.model.IBaseEntity;
import io.github.zwieback.familyfinance.core.preference.config.DatabasePrefs;
import io.reactivex.Single;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public abstract class EntityCreator<E extends IBaseEntity>
        implements Callable<Single<Iterable<E>>>, Comparator<E> {

    @NonNull
    protected final Context context;
    @NonNull
    protected final ReactiveEntityStore<Persistable> data;
    @NonNull
    protected final DatabasePrefs databasePrefs;

    protected EntityCreator(@NonNull Context context,
                            @NonNull ReactiveEntityStore<Persistable> data) {
        this.context = context;
        this.data = data;
        this.databasePrefs = DatabasePrefs.with(context);
    }

    @NonNull
    protected abstract Iterable<E> buildEntities();

    @Override
    public Single<Iterable<E>> call() {
        return data.insert(buildEntities());
    }

    @NonNull
    protected String getString(@StringRes int resId) {
        return context.getResources().getString(resId);
    }
}
