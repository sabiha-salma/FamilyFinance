package io.github.zwieback.familyfinance.core.lifecycle.destroyer;

import androidx.annotation.NonNull;

import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.Callable;

import io.reactivex.Single;

public abstract class EntityViewDestroyer implements Callable<Single<Boolean>> {

    private final Connection connection;

    protected EntityViewDestroyer(Connection connection) {
        this.connection = connection;
    }

    @NonNull
    protected abstract String getViewName();

    /**
     * @return {@code false} because "DROP VIEW" is DDL operation
     */
    @Override
    public Single<Boolean> call() {
        return Single.fromCallable(() -> {
            try (Statement statement = connection.createStatement()) {
                return statement.execute("DROP VIEW IF EXISTS " + getViewName());
            }
        });
    }
}
