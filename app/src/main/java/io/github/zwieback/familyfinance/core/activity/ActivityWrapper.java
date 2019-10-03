package io.github.zwieback.familyfinance.core.activity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import io.github.zwieback.familyfinance.R;

public abstract class ActivityWrapper extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView();
        Toolbar toolbar = findToolbar();
        if (toolbar == null) {
            setupActionBar();
        } else {
            setupToolbar(toolbar);
        }
    }

    protected abstract void setupContentView();

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getTitleStringId());
            actionBar.setDisplayHomeAsUpEnabled(isDisplayHomeAsUpEnabled());
        }
    }

    private void setupToolbar(@NonNull Toolbar toolbar) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getTitleStringId());
            getSupportActionBar().setDisplayHomeAsUpEnabled(isDisplayHomeAsUpEnabled());
        }
    }

    @Nullable
    protected final Toolbar findToolbar() {
        return findViewById(R.id.toolbar);
    }

    protected boolean isDisplayHomeAsUpEnabled() {
        return true;
    }

    @StringRes
    protected abstract int getTitleStringId();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
