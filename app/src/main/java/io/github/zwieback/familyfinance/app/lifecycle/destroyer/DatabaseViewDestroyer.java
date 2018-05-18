package io.github.zwieback.familyfinance.app.lifecycle.destroyer;

import android.util.Log;

import java.sql.Connection;

import io.github.zwieback.familyfinance.business.operation.lifecycle.destroyer.OperationViewDestroyer;
import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityViewDestroyer;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DatabaseViewDestroyer {

    private static final String TAG = "DatabaseViewDestroyer";

    private final Connection connection;

    public DatabaseViewDestroyer(Connection connection) {
        this.connection = connection;
    }

    public void destroyViews() {
        destroyView(new OperationViewDestroyer(connection), onOperationViewDestroyed());
    }

    private void destroyView(EntityViewDestroyer destroyer, Consumer<Boolean> onViewDestroyed) {
        Observable.fromCallable(destroyer)
                .flatMap(observable -> observable)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(onViewDestroyed);
    }

    private Consumer<Boolean> onOperationViewDestroyed() {
        return ignoredResult -> logFinishOfDestroyer(OperationViewDestroyer.class);
    }

    private static void logFinishOfDestroyer(Class destroyerClass) {
        Log.d(TAG, "Destroyer '" + destroyerClass.getSimpleName() + "' is finished");
    }
}
