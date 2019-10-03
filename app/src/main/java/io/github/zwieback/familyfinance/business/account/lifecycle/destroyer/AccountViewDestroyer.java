package io.github.zwieback.familyfinance.business.account.lifecycle.destroyer;

import androidx.annotation.NonNull;

import java.sql.Connection;

import io.github.zwieback.familyfinance.core.lifecycle.destroyer.EntityViewDestroyer;

public class AccountViewDestroyer extends EntityViewDestroyer {

    public AccountViewDestroyer(Connection connection) {
        super(connection);
    }

    @NonNull
    @Override
    protected String getViewName() {
        return "v_account";
    }
}
