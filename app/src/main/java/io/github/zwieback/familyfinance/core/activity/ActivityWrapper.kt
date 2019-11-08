package io.github.zwieback.familyfinance.core.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import io.github.zwieback.familyfinance.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class ActivityWrapper : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    protected open val isDisplayHomeAsUpEnabled: Boolean
        get() = true

    @get:StringRes
    protected abstract val titleStringId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupContentView()
        findToolbar()?.let { toolbar -> setupToolbar(toolbar) } ?: setupActionBar()
    }

    protected abstract fun setupContentView()

    protected fun findToolbar(): Toolbar? {
        return findViewById(R.id.toolbar)
    }

    private fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        setupActionBar()
    }

    private fun setupActionBar() {
        supportActionBar?.let { actionBar ->
            actionBar.setTitle(titleStringId)
            actionBar.setDisplayHomeAsUpEnabled(isDisplayHomeAsUpEnabled)
        }
    }

    /**
     * See ["to .clear or to .dispose" section](https://blog.kaush.co/2017/06/21/rxjava-1-rxjava-2-disposing-subscriptions/)
     */
    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Add disposable to unsubscribe from it on activity destroy.
     */
    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }
}
