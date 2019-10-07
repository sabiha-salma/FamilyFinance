package io.github.zwieback.familyfinance.core.activity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import io.github.zwieback.familyfinance.R;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class ActivityWrapper extends AppCompatActivity {

    @NonNull
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

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

    /**
     * @see <a href="https://blog.kaush.co/2017/06/21/rxjava-1-rxjava-2-disposing-subscriptions/">
     * "to .clear or to .dispose" section
     * </a>
     */
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

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

    /**
     * Add disposable to unsubscribe from it on activity destroy.
     */
    protected void addDisposable(@NonNull Disposable disposable) {
        compositeDisposable.add(disposable);
    }
}
